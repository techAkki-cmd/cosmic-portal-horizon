// ✅ FIXED: Import types instead of defining them locally
import type {
  LoginCredentials,
  SignupData,
  AuthResponse,
  BirthProfileData,
  UserProfile,
  DeviceType 
} from '../types/auth';

// ================ INTERFACES FOR INTERNAL USE ================

interface TokenPayload {
  sub: string;
  username: string;
  role: string;
  exp: number;
  iat: number;
}

interface ErrorResponse {
  message: string;
  error?: string;
  status?: number;
  timestamp?: string;
}

// ================ AUTH SERVICE CLASS ================

class AuthService {
  private readonly baseURL = import.meta.env.VITE_API_URL || 'http://localhost:8080/api';
  private readonly tokenKey = 'cosmic_auth_token';
  private readonly userKey = 'cosmic_user_data';
  private readonly refreshTokenKey = 'cosmic_refresh_token';
  private readonly tokenExpiryBuffer = 5 * 60 * 1000; // 5 minutes buffer before expiry
  
  // Track refresh attempts to prevent infinite loops
  private refreshPromise: Promise<string | null> | null = null;
  private isRefreshing = false;

  constructor() {
    // Initialize service and validate stored tokens
    this.initializeService();
  }

  // ================ INITIALIZATION ================

  private initializeService(): void {
    // Validate existing token on service initialization
    if (this.getToken() && !this.isAuthenticated()) {
      console.warn('🔒 Invalid token found, clearing authentication data');
      this.removeTokens();
    }
  }

  // ================ AUTHENTICATION METHODS ================

  /**
   * ✅ ENHANCED: Login method with comprehensive error handling and logging
   */
  async login(credentials: LoginCredentials | string, password?: string): Promise<AuthResponse> {
    console.log('📤 AuthService.login called');
    
    try {
      // Handle both object and string-based login
      let loginData: LoginCredentials;
      
      if (typeof credentials === 'string') {
        if (!password) {
          throw new Error('Password is required when using username/password login');
        }
        loginData = {
          username: credentials,
          password: password,
          userAgent: navigator.userAgent,
          deviceType: this.getDeviceType(),
          timezone: Intl.DateTimeFormat().resolvedOptions().timeZone
        };
      } else {
        loginData = {
          ...credentials,
          userAgent: credentials.userAgent || navigator.userAgent,
          deviceType: credentials.deviceType || this.getDeviceType(),
          timezone: credentials.timezone || Intl.DateTimeFormat().resolvedOptions().timeZone
        };
      }

      // ✅ ENHANCED: Validate required fields
      this.validateLoginData(loginData);

      // ✅ ENHANCED: Clean up and sanitize data
      const cleanLoginData = this.sanitizeLoginData(loginData);

      console.log('🔐 Sending login request...');
      const response = await fetch(`${this.baseURL}/auth/login`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Accept': 'application/json',
          'X-Client-Version': '1.0.0', // Add version tracking
        },
        body: JSON.stringify(cleanLoginData),
      });

      if (!response.ok) {
        const errorData = await this.handleErrorResponse(response);
        console.error('❌ Login failed:', errorData);
        throw new Error(errorData.message || `Login failed: ${response.status}`);
      }

      const data: AuthResponse = await response.json();
      console.log('🔍 Backend login response:', JSON.stringify(data, null, 2));
console.log('🔍 Response structure analysis:', {
  hasSuccess: 'success' in data,
  hasData: 'data' in data,  
  hasDirectToken: 'token' in data,
  hasWrappedToken: data.data && 'token' in data.data,
  topLevelKeys: Object.keys(data),
  dataKeys: data.data ? Object.keys(data.data) : 'no data object'
});
      console.log('✅ Login successful');
      
      // ✅ ENHANCED: Store authentication data with validation
      await this.storeAuthenticationData(data);
      
      return data;
    } catch (error) {
      console.error('🔥 AuthService.login error:', error);
      // Clear any partial authentication state
      this.removeTokens();
      throw error;
    }
  }

  /**
   * ✅ ENHANCED: Signup method with comprehensive validation
   */
  async signup(userData: SignupData): Promise<{ message: string; user?: UserProfile }> {
    console.log('📤 AuthService.signup called');
    
    try {
      // ✅ ENHANCED: Comprehensive validation
      this.validateSignupData(userData);

      // ✅ ENHANCED: Add client information with fallbacks
      const enhancedUserData: SignupData = {
        ...userData,
        userAgent: userData.userAgent || navigator.userAgent,
        deviceType: userData.deviceType || this.getDeviceType(),
        timezone: userData.timezone || this.getTimezone(),
        registrationSource: userData.registrationSource || 'ORGANIC',
        acceptedTermsVersion: userData.acceptedTermsVersion || '1.0',
        acceptedPrivacyVersion: userData.acceptedPrivacyVersion || '1.0'
      };

      const response = await fetch(`${this.baseURL}/auth/signup`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Accept': 'application/json',
          'X-Client-Version': '1.0.0',
        },
        body: JSON.stringify(enhancedUserData),
      });

      if (!response.ok) {
        const errorData = await this.handleErrorResponse(response);
        throw new Error(errorData.message || 'Signup failed');
      }

      const result = await response.json();
      console.log('✅ Signup successful');
      return result;
    } catch (error) {
      console.error('🔥 AuthService.signup error:', error);
      throw error;
    }
  }

  /**
   * ✅ ENHANCED: Birth profile update with retry logic
   */
  async updateBirthProfile(data: BirthProfileData): Promise<UserProfile> {
    console.log('📤 AuthService.updateBirthProfile called');
    
    try {
      // Validate birth profile data
      this.validateBirthProfileData(data);

      const response = await this.authenticatedRequest('/users/birth-profile', {
        method: 'PUT',
        body: JSON.stringify(data)
      });
      
      if (!response.ok) {
        const errorData = await this.handleErrorResponse(response);
        throw new Error(errorData.message || 'Failed to update birth profile');
      }
      
      const updatedProfile: UserProfile = await response.json();
      
      // ✅ ENHANCED: Update local storage with validation
      this.updateStoredUserProfile(updatedProfile);
      
      console.log('✅ Birth profile updated successfully');
      return updatedProfile;
    } catch (error) {
      console.error('🔥 AuthService.updateBirthProfile error:', error);
      throw error;
    }
  }

  // ================ USER PROFILE METHODS ================

  /**
   * ✅ ENHANCED: Get user profile with caching and validation
   */
  async getUserProfile(forceRefresh: boolean = false): Promise<UserProfile> {
    try {
      // Return cached profile if available and not forcing refresh
      if (!forceRefresh) {
        const cachedProfile = this.getCachedUserProfile();
        if (cachedProfile && this.isProfileValid(cachedProfile)) {
          return cachedProfile;
        }
      }

      console.log('📋 Fetching user profile from server...');
      const response = await this.authenticatedRequest('/users/profile');
      
      if (!response.ok) {
        if (response.status === 401) {
          console.warn('🔒 Unauthorized - session expired');
          await this.logout();
          throw new Error('Session expired. Please login again.');
        }
        const errorData = await this.handleErrorResponse(response);
        throw new Error(errorData.message || 'Failed to fetch user profile');
      }
      
      const profile: UserProfile = await response.json();
      
      // ✅ ENHANCED: Update local storage with validation
      this.updateStoredUserProfile(profile);
      
      console.log('✅ User profile fetched successfully');
      return profile;
    } catch (error) {
      console.error('❌ Error fetching user profile:', error);
      throw error;
    }
  }

  /**
   * ✅ ENHANCED: Update user profile with validation
   */
  async updateUserProfile(profileData: Partial<UserProfile>): Promise<UserProfile> {
    try {
      // Validate profile data
      if (!profileData || Object.keys(profileData).length === 0) {
        throw new Error('Profile data is required');
      }

      const response = await this.authenticatedRequest('/users/profile', {
        method: 'PUT',
        body: JSON.stringify(profileData)
      });
      
      if (!response.ok) {
        const errorData = await this.handleErrorResponse(response);
        throw new Error(errorData.message || 'Failed to update profile');
      }
      
      const updatedProfile: UserProfile = await response.json();
      this.updateStoredUserProfile(updatedProfile);
      
      console.log('✅ User profile updated successfully');
      return updatedProfile;
    } catch (error) {
      console.error('❌ Error updating user profile:', error);
      throw error;
    }
  }

  // ================ TOKEN MANAGEMENT ================

  getToken(): string | null {
    try {
      return localStorage.getItem(this.tokenKey);
    } catch (error) {
      console.error('Error accessing token from localStorage:', error);
      return null;
    }
  }

  setToken(token: string): void {
    try {
      if (!token || typeof token !== 'string') {
        throw new Error('Invalid token provided');
      }
      localStorage.setItem(this.tokenKey, token);
    } catch (error) {
      console.error('Error storing token:', error);
      throw new Error('Failed to store authentication token');
    }
  }

  getRefreshToken(): string | null {
    try {
      return localStorage.getItem(this.refreshTokenKey);
    } catch (error) {
      console.error('Error accessing refresh token:', error);
      return null;
    }
  }

  setRefreshToken(token: string): void {
    try {
      if (!token || typeof token !== 'string') {
        throw new Error('Invalid refresh token provided');
      }
      localStorage.setItem(this.refreshTokenKey, token);
    } catch (error) {
      console.error('Error storing refresh token:', error);
      throw new Error('Failed to store refresh token');
    }
  }

  removeTokens(): void {
    try {
      localStorage.removeItem(this.tokenKey);
      localStorage.removeItem(this.refreshTokenKey);
      localStorage.removeItem(this.userKey);
      console.log('🧹 Authentication data cleared');
    } catch (error) {
      console.error('Error removing tokens:', error);
    }
  }

  /**
   * ✅ ENHANCED: Improved token validation with better error handling
   */
  isAuthenticated(): boolean {
    try {
      const token = this.getToken();
      
      if (!token) {
        return false;
      }

      // Validate token format
      if (!this.isValidJWTFormat(token)) {
        console.warn('🔒 Invalid JWT format detected');
        this.removeTokens();
        return false;
      }

      const payload = this.decodeTokenPayload(token);
      if (!payload) {
        this.removeTokens();
        return false;
      }

      const expiry = payload.exp * 1000;
      const now = Date.now();
      const isValid = now < (expiry - this.tokenExpiryBuffer);
      
      if (!isValid) {
        console.warn('🔒 Token expired, clearing authentication data');
        this.removeTokens();
      }
      
      return isValid;
    } catch (error) {
      console.error('❌ Error validating token:', error);
      this.removeTokens();
      return false;
    }
  }

  /**
   * ✅ ENHANCED: Improved refresh token with concurrency handling
   */
  async refreshToken(): Promise<string | null> {
    // Prevent concurrent refresh attempts
    if (this.isRefreshing && this.refreshPromise) {
      return this.refreshPromise;
    }

    this.isRefreshing = true;
    this.refreshPromise = this.performTokenRefresh();
    
    try {
      const result = await this.refreshPromise;
      return result;
    } finally {
      this.isRefreshing = false;
      this.refreshPromise = null;
    }
  }

  private async performTokenRefresh(): Promise<string | null> {
    try {
      const refreshToken = this.getRefreshToken();
      if (!refreshToken) {
        throw new Error('No refresh token available');
      }

      console.log('🔄 Refreshing authentication token...');
      const response = await fetch(`${this.baseURL}/auth/refresh`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Accept': 'application/json',
        },
        body: JSON.stringify({ refreshToken }),
      });

      if (!response.ok) {
        const errorData = await this.handleErrorResponse(response);
        throw new Error(errorData.message || 'Failed to refresh token');
      }

      const data = await response.json();
      
      if (!data.token) {
        throw new Error('No token received from refresh endpoint');
      }

      // Store new tokens
      this.setToken(data.token);
      if (data.refreshToken) {
        this.setRefreshToken(data.refreshToken);
      }

      console.log('✅ Token refreshed successfully');
      return data.token;
    } catch (error) {
      console.error('❌ Error refreshing token:', error);
      await this.logout();
      return null;
    }
  }

  // ================ AUTHENTICATED REQUESTS ================

  /**
   * ✅ ENHANCED: Authenticated request with automatic token refresh and retry
   */
  async authenticatedRequest(url: string, options: RequestInit = {}): Promise<Response> {
    let token = this.getToken();
    
    if (!token) {
      throw new Error('No authentication token available. Please log in again.');
    }

    const requestOptions: RequestInit = {
      ...options,
      headers: {
        'Authorization': `Bearer ${token}`,
        'Content-Type': 'application/json',
        'Accept': 'application/json',
        ...options.headers,
      },
    };

    try {
      let response = await fetch(`${this.baseURL}${url}`, requestOptions);

      // Handle token expiration with automatic retry
      if (response.status === 401 && !this.isRefreshing) {
        console.log('🔄 Token expired, attempting refresh...');
        const refreshedToken = await this.refreshToken();
        
        if (refreshedToken) {
          // Retry with new token
          const retryOptions: RequestInit = {
            ...options,
            headers: {
              'Authorization': `Bearer ${refreshedToken}`,
              'Content-Type': 'application/json',
              'Accept': 'application/json',
              ...options.headers,
            },
          };
          response = await fetch(`${this.baseURL}${url}`, retryOptions);
          console.log('✅ Request retried with refreshed token');
        } else {
          throw new Error('Unable to refresh authentication token');
        }
      }

      return response;
    } catch (error) {
      console.error('❌ Authenticated request failed:', error);
      throw error;
    }
  }

  // ================ UTILITY METHODS ================

  /**
   * ✅ ENHANCED: Get current user with validation
   */
  getCurrentUser(): UserProfile | null {
    return this.getCachedUserProfile();
  }

  /**
   * ✅ ENHANCED: Enhanced device type detection
   */
  // In AuthService.ts - update the getDeviceType function

/**
 * ✅ FIXED: Return proper DeviceType union instead of string
 */
private getDeviceType(): DeviceType {
  const userAgent = navigator.userAgent.toLowerCase();
  
  // Mobile devices
  if (/android|webos|iphone|ipod|blackberry|iemobile|opera mini/i.test(userAgent)) {
    return 'MOBILE';
  }
  
  // Tablets
  if (/ipad|android(?!.*mobile)|tablet|kindle|silk/i.test(userAgent)) {
    return 'TABLET';
  }
  
  // Smart TVs and other devices
  if (/smart-tv|smarttv|googletv|appletv|hbbtv|pov_tv|netcast.tv/i.test(userAgent)) {
    return 'TV';
  }
  
  return 'DESKTOP';
}


  /**
   * ✅ ENHANCED: Get timezone with fallback
   */
  private getTimezone(): string {
    try {
      return Intl.DateTimeFormat().resolvedOptions().timeZone;
    } catch (error) {
      console.warn('Unable to detect timezone, using default');
      return 'UTC';
    }
  }

  /**
   * ✅ ENHANCED: Improved error response handling
   */
  private async handleErrorResponse(response: Response): Promise<ErrorResponse> {
    try {
      const contentType = response.headers.get('content-type');
      
      if (contentType && contentType.includes('application/json')) {
        const errorData = await response.json();
        return {
          message: errorData.message || errorData.error || 'An error occurred',
          error: errorData.error,
          status: response.status,
          timestamp: errorData.timestamp
        };
      } else {
        const text = await response.text();
        return {
          message: text || `HTTP ${response.status}: ${response.statusText}`,
          status: response.status
        };
      }
    } catch (parseError) {
      console.error('Error parsing error response:', parseError);
      return {
        message: `HTTP ${response.status}: ${response.statusText}`,
        status: response.status
      };
    }
  }

  // ================ VALIDATION METHODS ================

  private validateLoginData(data: LoginCredentials): void {
    if (!data.username || !data.password) {
      throw new Error('Username and password are required');
    }

    if (data.username.length < 3) {
      throw new Error('Username must be at least 3 characters long');
    }

    if (data.password.length < 6) {
      throw new Error('Password must be at least 6 characters long');
    }
  }

  private validateSignupData(data: SignupData): void {
    const required = ['username', 'email', 'password', 'firstName'];
    const missing = required.filter(field => !data[field as keyof SignupData]);
    
    if (missing.length > 0) {
      throw new Error(`Required fields missing: ${missing.join(', ')}`);
    }

    if (!data.agreeToTerms || !data.agreeToPrivacyPolicy || !data.ageConfirmation) {
      throw new Error('You must agree to the terms, privacy policy, and confirm your age');
    }

    // Email validation
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailRegex.test(data.email)) {
      throw new Error('Please enter a valid email address');
    }

    // Password strength validation
    if (data.password.length < 8) {
      throw new Error('Password must be at least 8 characters long');
    }
  }

  private validateBirthProfileData(data: BirthProfileData): void {
    if (!data.birthDateTime) {
      throw new Error('Birth date and time are required');
    }

    if (!data.birthLocation) {
      throw new Error('Birth location is required');
    }

    if (data.birthLatitude === undefined || data.birthLongitude === undefined) {
      throw new Error('Birth coordinates are required');
    }

    // Validate coordinate ranges
    if (Math.abs(data.birthLatitude) > 90) {
      throw new Error('Latitude must be between -90 and 90 degrees');
    }

    if (Math.abs(data.birthLongitude) > 180) {
      throw new Error('Longitude must be between -180 and 180 degrees');
    }
  }

  private sanitizeLoginData(data: LoginCredentials): LoginCredentials {
    return {
      ...data,
      username: data.username.trim().toLowerCase(),
      password: data.password.trim(),
      email: data.email?.trim().toLowerCase()
    };
  }

  // ================ TOKEN UTILITIES ================

  private isValidJWTFormat(token: string): boolean {
    const parts = token.split('.');
    return parts.length === 3 && parts.every(part => part.length > 0);
  }

  private decodeTokenPayload(token: string): TokenPayload | null {
    try {
      const payload = JSON.parse(atob(token.split('.')[1]));
      return payload as TokenPayload;
    } catch (error) {
      console.error('Error decoding token payload:', error);
      return null;
    }
  }

  // ================ USER PROFILE UTILITIES ================

  private getCachedUserProfile(): UserProfile | null {
    try {
      const userData = localStorage.getItem(this.userKey);
      return userData ? JSON.parse(userData) : null;
    } catch (error) {
      console.error('Error parsing cached user data:', error);
      localStorage.removeItem(this.userKey);
      return null;
    }
  }

  private updateStoredUserProfile(profile: UserProfile): void {
    try {
      localStorage.setItem(this.userKey, JSON.stringify(profile));
    } catch (error) {
      console.error('Error storing user profile:', error);
    }
  }

  private isProfileValid(profile: UserProfile): boolean {
    // Basic validation - you can enhance this based on your needs
    return !!(profile && profile.id && profile.username && profile.email);
  }

  // ================ AUTHENTICATION DATA STORAGE ================

  /**
 * ✅ FIXED: Handle wrapped response format from backend
 */
private async storeAuthenticationData(authResponse: any): Promise<void> {
  try {
    console.log('📝 Storing authentication data:', authResponse);
    
    // ✅ FIXED: Handle both direct and wrapped response formats
    const responseData = authResponse.data || authResponse;
    const token = responseData.token;
    
    if (!token) {
      console.error('❌ No token found in response structure:', {
        hasDirectToken: !!authResponse.token,
        hasWrappedToken: !!(authResponse.data && authResponse.data.token),
        responseKeys: Object.keys(authResponse),
        dataKeys: authResponse.data ? Object.keys(authResponse.data) : 'no data object'
      });
      throw new Error('No authentication token in response');
    }

    // Store tokens
    this.setToken(token);
    
    if (responseData.refreshToken) {
      this.setRefreshToken(responseData.refreshToken);
    }

    // Store user profile if included
    if (responseData.user) {
      this.updateStoredUserProfile(responseData.user);
    }

    console.log('✅ Authentication data stored successfully');
  } catch (error) {
    console.error('❌ Error storing authentication data:', error);
    throw new Error('Failed to store authentication data');
  }
}


  // ================ LOGOUT ================

  /**
   * ✅ ENHANCED: Comprehensive logout with server notification
   */
  /**
 * ✅ ENHANCED: Comprehensive logout with complete cleanup
 */
async logout(): Promise<void> {
  try {
    const token = this.getToken();
    
    if (token) {
      console.log('📤 Notifying server of logout...');
      try {
        await fetch(`${this.baseURL}/auth/logout`, {
          method: 'POST',
          headers: {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json',
          },
        });
        console.log('✅ Server notified of logout');
      } catch (error) {
        console.warn('⚠️ Failed to notify server of logout:', error);
        // Continue with local logout even if server notification fails
      }
    }
  } catch (error) {
    console.error('❌ Logout error:', error);
  } finally {
    // ✅ ALWAYS: Clear all local data regardless of server response
    this.forceCompleteTokenCleanup();
    console.log('✅ Local logout completed');
  }
}

/**
 * ✅ NEW: Force complete token and data cleanup
 */
private forceCompleteTokenCleanup(): void {
  try {
    // Remove all auth-related items
    const authKeys = [
      this.tokenKey,
      this.userKey, 
      this.refreshTokenKey,
      'cosmic_auth_token',
      'cosmic_user_data',
      'cosmic_refresh_token'
    ];
    
    authKeys.forEach(key => {
      localStorage.removeItem(key);
      sessionStorage.removeItem(key);
    });
    
    // Clear any keys starting with 'cosmic_'
    Object.keys(localStorage).forEach(key => {
      if (key.startsWith('cosmic_')) {
        localStorage.removeItem(key);
      }
    });
    
    console.log('🧹 Complete token cleanup finished');
  } catch (error) {
    console.error('Error during token cleanup:', error);
  }
}

  // ================ ADDITIONAL METHODS ================

  /**
   * ✅ ENHANCED: Change password with validation
   */
  async changePassword(currentPassword: string, newPassword: string): Promise<void> {
    try {
      if (!currentPassword || !newPassword) {
        throw new Error('Current password and new password are required');
      }

      if (newPassword.length < 8) {
        throw new Error('New password must be at least 8 characters long');
      }

      if (currentPassword === newPassword) {
        throw new Error('New password must be different from current password');
      }

      const response = await this.authenticatedRequest('/users/change-password', {
        method: 'POST',
        body: JSON.stringify({ currentPassword, newPassword })
      });

      if (!response.ok) {
        const errorData = await this.handleErrorResponse(response);
        throw new Error(errorData.message || 'Failed to change password');
      }

      console.log('✅ Password changed successfully');
    } catch (error) {
      console.error('❌ Error changing password:', error);
      throw error;
    }
  }

  /**
   * ✅ ENHANCED: Request password reset with validation
   */
  async requestPasswordReset(email: string): Promise<void> {
    try {
      if (!email) {
        throw new Error('Email is required');
      }

      const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
      if (!emailRegex.test(email)) {
        throw new Error('Please enter a valid email address');
      }

      const response = await fetch(`${this.baseURL}/auth/forgot-password`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ email: email.trim().toLowerCase() }),
      });

      if (!response.ok) {
        const errorData = await this.handleErrorResponse(response);
        throw new Error(errorData.message || 'Failed to request password reset');
      }

      console.log('✅ Password reset email sent');
    } catch (error) {
      console.error('❌ Error requesting password reset:', error);
      throw error;
    }
  }

  /**
   * ✅ ENHANCED: Email verification with validation
   */
  async verifyEmail(token: string): Promise<void> {
    try {
      if (!token) {
        throw new Error('Verification token is required');
      }

      const response = await fetch(`${this.baseURL}/auth/verify-email`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ token }),
      });

      if (!response.ok) {
        const errorData = await this.handleErrorResponse(response);
        throw new Error(errorData.message || 'Failed to verify email');
      }

      console.log('✅ Email verified successfully');
    } catch (error) {
      console.error('❌ Error verifying email:', error);
      throw error;
    }
  }

  // ================ HEALTH CHECK ================

  /**
   * ✅ NEW: Health check method for monitoring service status
   */
  async healthCheck(): Promise<boolean> {
    try {
      const response = await fetch(`${this.baseURL}/health`, {
        method: 'GET',
        headers: {
          'Accept': 'application/json',
        },
      });
      return response.ok;
    } catch (error) {
      console.error('❌ Health check failed:', error);
      return false;
    }
  }
}

// ================ EXPORTS ================

export const authService = new AuthService();
export default authService;

// ✅ Types are exported from types/auth.ts
