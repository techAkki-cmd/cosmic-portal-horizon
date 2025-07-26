export interface LoginCredentials {
  username: string;
  password: string;
}

export interface SignupData {
  username: string;
  email: string;
  password: string;
  firstName?: string;
  lastName?: string;
  birthDateTime?: string;
  birthLocation?: string;
  birthLatitude?: number;
  birthLongitude?: number;
  timezone?: string;
}

export interface AuthResponse {
  token: string;
  type: string;
  username: string;
  email: string;
}

// NEW: Interface for birth profile data
export interface BirthProfileData {
  birthDateTime: string;
  birthLocation: string;
  birthLatitude?: number | null;
  birthLongitude?: number | null;
  timezone?: string;
}

class AuthService {
  private baseURL = import.meta.env.VITE_API_URL || 'http://localhost:8080/api';
  private tokenKey = 'cosmic_auth_token';
  private userKey = 'cosmic_user_data';

  // ✅ LOGIN METHOD
  async login(credentials: LoginCredentials): Promise<AuthResponse> {
    console.log('📤 AuthService.login called with:', { username: credentials.username, password: '***' });
    
    try {
      // Validate input
      if (!credentials.username || !credentials.password) {
        throw new Error('Username and password are required');
      }

      const loginData = {
        username: credentials.username.trim(),
        password: credentials.password.trim()
      };

      const response = await fetch(`${this.baseURL}/auth/login`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Accept': 'application/json',
        },
        body: JSON.stringify(loginData),
      });

      if (!response.ok) {
        const errorText = await response.text();
        let errorMessage = 'Login failed';
        try {
          const errorData = JSON.parse(errorText);
          errorMessage = errorData.message || errorMessage;
        } catch {
          errorMessage = errorText || errorMessage;
        }
        throw new Error(errorMessage);
      }

      const data: AuthResponse = await response.json();
      
      // Store token
      if (data.token) {
        this.setToken(data.token);
      }
      
      return data;
    } catch (error) {
      console.error('🔥 AuthService.login error:', error);
      throw error;
    }
  }

  // ✅ SIGNUP METHOD
  async signup(userData: SignupData): Promise<{ message: string }> {
    console.log('📤 AuthService.signup called with:', { username: userData.username, email: userData.email });
    
    try {
      if (!userData.username || !userData.email || !userData.password) {
        throw new Error('Username, email, and password are required');
      }

      const response = await fetch(`${this.baseURL}/auth/signup`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Accept': 'application/json',
        },
        body: JSON.stringify(userData),
      });

      if (!response.ok) {
        const errorText = await response.text();
        let errorMessage = 'Signup failed';
        try {
          const errorData = JSON.parse(errorText);
          errorMessage = errorData.message || errorMessage;
        } catch {
          errorMessage = errorText || errorMessage;
        }
        throw new Error(errorMessage);
      }

      return await response.json();
    } catch (error) {
      console.error('🔥 AuthService.signup error:', error);
      throw error;
    }
  }

  // ✅ NEW: UPDATE BIRTH PROFILE METHOD
  async updateBirthProfile(data: BirthProfileData): Promise<void> {
    console.log('📤 AuthService.updateBirthProfile called with:', data);
    
    try {
      const response = await this.authenticatedRequest('/user/birth-profile', {
        method: 'PUT',
        body: JSON.stringify(data)
      });
      
      if (!response.ok) {
        const errorText = await response.text();
        let errorMessage = 'Failed to update birth profile';
        try {
          const errorData = JSON.parse(errorText);
          errorMessage = errorData.message || errorMessage;
        } catch {
          errorMessage = errorText || errorMessage;
        }
        throw new Error(errorMessage);
      }
      
      // Update local storage with new profile data
      try {
        const updatedProfile = await response.json();
        localStorage.setItem(this.userKey, JSON.stringify(updatedProfile));
      } catch {
        // If response doesn't contain JSON, just refresh user profile
        const profile = await this.getUserProfile();
        localStorage.setItem(this.userKey, JSON.stringify(profile));
      }
      
    } catch (error) {
      console.error('🔥 AuthService.updateBirthProfile error:', error);
      throw error;
    }
  }

  // ✅ TOKEN MANAGEMENT METHODS
  getToken(): string | null {
    return localStorage.getItem(this.tokenKey);
  }

  setToken(token: string): void {
    localStorage.setItem(this.tokenKey, token);
  }

  removeToken(): void {
    localStorage.removeItem(this.tokenKey);
    localStorage.removeItem(this.userKey);
  }

  isAuthenticated(): boolean {
    const token = this.getToken();
    
    if (!token) {
      return false;
    }

    try {
      const payload = JSON.parse(atob(token.split('.')[1]));
      const expiry = payload.exp * 1000;
      return Date.now() < expiry;
    } catch {
      return false;
    }
  }

  async getUserProfile(): Promise<any> {
    const response = await this.authenticatedRequest('/user/profile');
    
    if (!response.ok) {
      throw new Error('Failed to fetch user profile');
    }
    
    return await response.json();
  }

  getCurrentUser(): any | null {
    const userData = localStorage.getItem(this.userKey);
    return userData ? JSON.parse(userData) : null;
  }

  async authenticatedRequest(url: string, options: RequestInit = {}): Promise<Response> {
    const token = this.getToken();
    
    if (!token) {
      throw new Error('No authentication token');
    }

    const requestOptions: RequestInit = {
      ...options,
      headers: {
        'Authorization': `Bearer ${token}`,
        'Content-Type': 'application/json',
        ...options.headers,
      },
    };

    return await fetch(`${this.baseURL}${url}`, requestOptions);
  }

  async logout(): Promise<void> {
    try {
      const token = this.getToken();
      
      if (token) {
        await fetch(`${this.baseURL}/auth/logout`, {
          method: 'POST',
          headers: {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json',
          },
        });
      }
    } catch (error) {
      console.error('Logout error:', error);
    } finally {
      this.removeToken();
      window.location.href = '/login';
    }
  }
}

// ✅ CRITICAL: Proper export
export const authService = new AuthService();
export default authService;
