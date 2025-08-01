import React, { createContext, useContext, useState, useEffect, ReactNode, useCallback } from 'react';
import { authService } from '../services/authService';

// ✅ FIXED: Import all types from central types file
import type { 
  UserProfile, // ✅ CHANGED: Use UserProfile instead of User for consistency
  LoginCredentials, 
  SignupData,
  AuthResponse,
  DeviceType
} from '../types/auth';

// ================ CONTEXT TYPE INTERFACE ================

interface AuthContextType {
  user: UserProfile | null;
  isAuthenticated: boolean;
  isLoading: boolean;
  isInitialized: boolean; // ✅ NEW: Track initialization state
  login: (loginData: LoginCredentials | string, password?: string) => Promise<void>;
  register: (signupData: SignupData) => Promise<{ message: string }>;
  logout: () => Promise<void>;
  updateUser: (user: UserProfile) => void;
  refreshUser: () => Promise<void>;
  error: string | null; // ✅ NEW: Global error state
  clearError: () => void; // ✅ NEW: Clear error function
}

// ================ CONTEXT CREATION ================

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (context === undefined) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
};

// ================ AUTH PROVIDER ================

interface AuthProviderProps {
  children: ReactNode;
}

export const AuthProvider: React.FC<AuthProviderProps> = ({ children }) => {
  const [user, setUser] = useState<UserProfile | null>(null);
  const [isLoading, setIsLoading] = useState(false);
  const [isInitialized, setIsInitialized] = useState(false);
  const [error, setError] = useState<string | null>(null);

  // ================ INITIALIZATION ================

  useEffect(() => {
    initializeAuth();
  }, []);

  /**
   * ✅ ENHANCED: Improved initialization with better error handling
   */
  const initializeAuth = async () => {
    try {
      setIsLoading(true);
      setError(null);
      
      console.log('🚀 Initializing authentication...');
      
      // ✅ FIXED: Check if authenticated first, then get profile
      if (authService.isAuthenticated()) {
        console.log('🔐 Valid token found, fetching user profile...');
        
        try {
          // Get cached profile first for faster loading
          const cachedUser = authService.getCurrentUser();
          if (cachedUser && isValidUserProfile(cachedUser)) {
            console.log('📦 Using cached user profile');
            const userData = createUserData(cachedUser);
            setUser(userData);
          }
          
          // Then refresh from server in background
          const currentUser = await authService.getUserProfile();
          const userData = createUserData(currentUser);
          setUser(userData);
          
          console.log('✅ Authentication initialized successfully');
        } catch (profileError) {
          console.error('❌ Error fetching user profile during init:', profileError);
          
          // If profile fetch fails, clear authentication
          await authService.logout();
          setUser(null);
          
          // Don't set error here as user might not be logged in
        }
      } else {
        console.log('🔒 No valid authentication found');
        setUser(null);
      }
    } catch (error) {
      console.error('❌ Auth initialization error:', error);
      setUser(null);
      // Clear any invalid tokens
      await authService.logout();
    } finally {
      setIsLoading(false);
      setIsInitialized(true);
    }
  };

  // ================ UTILITY FUNCTIONS ================

  /**
   * ✅ ENHANCED: Improved user data creation with validation
   */
  /**
 * ✅ ENHANCED: Debug and create user data with robust validation
 */
/**
 * ✅ ENHANCED DEBUG: See exactly what the backend is returning
 */
const createUserData = (userProfile: any): UserProfile => {
  try {
    // ✅ CRITICAL DEBUG: Log the complete object structure
    console.log('🔍 COMPLETE userProfile object:', userProfile);
    console.log('🔍 userProfile type:', typeof userProfile);
    console.log('🔍 userProfile keys:', userProfile ? Object.keys(userProfile) : 'null/undefined');
    console.log('🔍 Stringified userProfile:', JSON.stringify(userProfile, null, 2));
    console.log('🔍 Direct property access:', {
      directId: userProfile?.id,
      directUsername: userProfile?.username,
      directEmail: userProfile?.email,
      hasDataProperty: 'data' in (userProfile || {}),
      dataContents: userProfile?.data ? Object.keys(userProfile.data) : 'no data property'
    });

    // ✅ ENHANCED: Handle different response structures
    let actualUserData = userProfile;

    // Check if data is wrapped in a 'data' property
    if (userProfile && userProfile.data && typeof userProfile.data === 'object') {
      console.log('🔍 Found wrapped data, unwrapping...');
      actualUserData = userProfile.data;
      console.log('🔍 Unwrapped data:', actualUserData);
      console.log('🔍 Unwrapped data keys:', Object.keys(actualUserData));
    }

    // Check if data is wrapped in a 'user' property
    if (userProfile && userProfile.user && typeof userProfile.user === 'object') {
      console.log('🔍 Found user wrapper, unwrapping...');
      actualUserData = userProfile.user;
      console.log('🔍 Unwrapped user data:', actualUserData);
    }

    // Validate the actual user data
    if (!actualUserData) {
      console.error('❌ actualUserData is null/undefined');
      throw new Error('User profile data is null or undefined');
    }

    if (typeof actualUserData !== 'object') {
      console.error('❌ actualUserData is not an object:', typeof actualUserData);
      throw new Error('User profile data is not an object');
    }

    // Check for required fields with detailed logging
    const hasId = 'id' in actualUserData && actualUserData.id != null;
    const hasUsername = 'username' in actualUserData && actualUserData.username != null;
    
    console.log('🔍 Field validation:', {
      hasId,
      hasUsername,
      idValue: actualUserData.id,
      usernameValue: actualUserData.username,
      allFields: Object.entries(actualUserData)
    });

    if (!hasId && !hasUsername) {
      console.error('❌ Missing both id and username:', actualUserData);
      console.error('❌ Available fields:', Object.keys(actualUserData));
      throw new Error('User profile missing both id and username');
    }

    if (!hasUsername) {
      console.error('❌ Missing username:', actualUserData);
      throw new Error('User profile missing username');
    }

    // ✅ ENHANCED: Create user data with fallbacks
    const displayName = actualUserData.firstName 
      ? `${actualUserData.firstName} ${actualUserData.lastName || ''}`.trim()
      : actualUserData.username;

    const userData: UserProfile = {
      id: actualUserData.id || actualUserData.username,
      username: actualUserData.username,
      email: actualUserData.email || '',
      firstName: actualUserData.firstName || '',
      lastName: actualUserData.lastName || '',
      fullName: displayName,
      displayName: displayName,
      profileCompletionPercentage: actualUserData.profileCompletionPercentage ?? 0,
      role: (actualUserData.role || 'CLIENT') as 'CLIENT' | 'ASTROLOGER' | 'ADMIN',
      enabled: actualUserData.enabled ?? true,
      emailVerified: actualUserData.emailVerified ?? false,
      
      // Spread any additional properties
      ...actualUserData
    };

    console.log('✅ Successfully created userData:', userData);
    return userData;

  } catch (error) {
    console.error('❌ Error in createUserData:', error);
    console.error('❌ Original data:', userProfile);
    throw new Error(`Invalid user profile data: ${error.message}`);
  }
};


  /**
   * ✅ NEW: Validate user profile structure
   */
  const isValidUserProfile = (profile: any): profile is UserProfile => {
    return profile && 
           typeof profile.id !== 'undefined' && 
           typeof profile.username === 'string' && 
           typeof profile.email === 'string';
  };

  /**
   * ✅ NEW: Clear error state
   */
  const clearError = useCallback(() => {
    setError(null);
  }, []);

  // ================ AUTH METHODS ================

  /**
   * ✅ ENHANCED: Improved login with comprehensive error handling
   */
  /**
 * ✅ ENHANCED: Handle both direct and wrapped user profile responses
 */
/**
 * ✅ ENHANCED: Login with pre-cleanup
 */
/**
 * ✅ FIXED: Login method with proper variable declaration
 */
const login = async (loginData: LoginCredentials | string, password?: string): Promise<void> => {
  try {
    setIsLoading(true);
    setError(null);
    
    // Clear existing state
    setUser(null);
    await forceCompleteCleanup();
    
    console.log('🔐 Starting fresh login process...');
    
    // ✅ FIXED: Properly declare authData variable
    let authData: LoginCredentials;

    // Support both comprehensive object and simple username/password
    if (typeof loginData === 'string') {
      if (!password) {
        throw new Error('Password is required when using username/password login');
      }
      authData = {
        username: loginData,
        password: password,
        userAgent: navigator.userAgent,
        deviceType: getDeviceType(),
        timezone: getTimezone(),
        rememberMe: true
      };
    } else {
      authData = {
        ...loginData,
        userAgent: loginData.userAgent || navigator.userAgent,
        deviceType: loginData.deviceType || getDeviceType(),
        timezone: loginData.timezone || getTimezone()
      };
    }

    // Validate required fields
    if (!authData.username?.trim() || !authData.password?.trim()) {
      throw new Error('Username and password are required');
    }

    // ✅ AUTHENTICATE - now authData is properly defined
    console.log('📤 Sending login request...');
    const authResponse: AuthResponse = await authService.login(authData);
    console.log('✅ Login successful, processing user data...');
    console.log('🔍 Full authResponse:', JSON.stringify(authResponse, null, 2));

    // ✅ ENHANCED: Try multiple sources for user data
    let userProfileData: any = null;
    
    // Try different locations for user data
    if (authResponse.user) {
      console.log('📋 Using user data from authResponse.user');
      userProfileData = authResponse.user;
    } else if (authResponse.data && authResponse.data.user) {
      console.log('📋 Using user data from authResponse.data.user');
      userProfileData = authResponse.data.user;
    } else if (authResponse.data) {
      console.log('📋 Using direct data from authResponse.data');
      userProfileData = authResponse.data;
    } else {
      // Fallback: fetch user profile separately
      console.log('📋 No user data in auth response, fetching separately...');
      await new Promise(resolve => setTimeout(resolve, 300));
      
      const userProfile = await authService.getUserProfile(true);
      console.log('🔍 Fetched userProfile:', JSON.stringify(userProfile, null, 2));
      
      // Handle wrapped user profile response
      if (userProfile.data) {
        userProfileData = userProfile.data;
      } else if (userProfile.user) {
        userProfileData = userProfile.user;
      } else {
        userProfileData = userProfile;
      }
    }

    console.log('🔍 Final userProfileData before createUserData:', userProfileData);

    // ✅ CREATE USER DATA
    if (userProfileData) {
      const userData = createUserData(userProfileData);
      setUser(userData);
      console.log('✅ Login completed with user data');
    } else {
      throw new Error('No user data received from any source');
    }
    
  } catch (error: any) {
    console.error('❌ Login failed:', error);
    setUser(null);
    await forceCompleteCleanup();
    
    let errorMessage = 'Login failed. Please try again.';
    if (error.message?.includes('authData is not defined')) {
      errorMessage = 'Login configuration error. Please refresh the page and try again.';
    } else if (error.message?.includes('missing both id and username')) {
      errorMessage = 'Login successful but user profile data is incomplete. Please contact support.';
    } else if (error.message?.includes('Invalid user profile data')) {
      errorMessage = 'Login successful but unable to load user profile. Please try again.';
    } else if (error.message) {
      errorMessage = error.message;
    }
    
    setError(errorMessage);
    throw new Error(errorMessage);
  } finally {
    setIsLoading(false);
  }
};


  /**
   * ✅ ENHANCED: Improved registration without auto-login
   */
  const register = async (signupData: SignupData): Promise<{ message: string }> => {
    try {
      setIsLoading(true);
      setError(null);
      
      console.log('📝 Starting registration process...');
      
      // Validate required fields
      if (!signupData.username?.trim() || !signupData.email?.trim() || 
          !signupData.password?.trim() || !signupData.firstName?.trim()) {
        throw new Error('Username, email, password, and first name are required');
      }

      if (!signupData.agreeToTerms || !signupData.agreeToPrivacyPolicy || !signupData.ageConfirmation) {
        throw new Error('You must agree to the terms, privacy policy, and confirm your age');
      }

      // Add client information if not provided
      const enhancedSignupData: SignupData = {
        ...signupData,
        userAgent: signupData.userAgent || navigator.userAgent,
        deviceType: signupData.deviceType || getDeviceType(),
        timezone: signupData.timezone || getTimezone(),
        registrationSource: signupData.registrationSource || 'ORGANIC',
        acceptedTermsVersion: '1.0',
        acceptedPrivacyVersion: '1.0'
      };

      const result = await authService.signup(enhancedSignupData);
      console.log('✅ Registration completed successfully');
      
      return result;
      
    } catch (error: any) {
      console.error('❌ Registration failed:', error);
      
      let errorMessage = 'Registration failed. Please try again.';
      
      if (error.message?.includes('Username already exists')) {
        errorMessage = 'This username is already taken. Please choose another.';
      } else if (error.message?.includes('Email already exists')) {
        errorMessage = 'An account with this email already exists.';
      } else if (error.message?.includes('Invalid email')) {
        errorMessage = 'Please enter a valid email address.';
      } else if (error.message) {
        errorMessage = error.message;
      }
      
      setError(errorMessage);
      throw new Error(errorMessage);
    } finally {
      setIsLoading(false);
    }
  };

  /**
   * ✅ ENHANCED: Improved logout with cleanup
   */
  /**
 * ✅ ENHANCED: Complete logout with thorough cleanup
 */
const logout = async (): Promise<void> => {
  try {
    setIsLoading(true);
    setError(null);
    
    console.log('📤 Starting comprehensive logout...');
    
    // ✅ STEP 1: Clear user state immediately
    setUser(null);
    
    // ✅ STEP 2: Notify server of logout (optional, don't wait for it)
    try {
      await authService.logout();
    } catch (error) {
      console.warn('⚠️ Server logout notification failed:', error);
      // Continue with local cleanup even if server notification fails
    }
    
    // ✅ STEP 3: Force complete cleanup
    await forceCompleteCleanup();
    
    console.log('✅ Logout completed successfully');
    
  } catch (error) {
    console.error('❌ Logout error:', error);
  } finally {
    // ✅ ENSURE: Always clear state regardless of errors
    setUser(null);
    setIsLoading(false);
    console.log('✅ User state cleared');
  }
};

/**
 * ✅ NEW: Force complete cleanup of all cached data
 */
const forceCompleteCleanup = async (): Promise<void> => {
  try {
    console.log('🧹 Performing complete cleanup...');
    
    // Clear all possible localStorage keys
    const keysToRemove = [
      'cosmic_auth_token',
      'cosmic_user_data', 
      'cosmic_refresh_token',
      // Add any other keys your app might use
      'userProfile',
      'authData',
      'currentUser'
    ];
    

    keysToRemove.forEach(key => {
      localStorage.removeItem(key);
      sessionStorage.removeItem(key); // Also clear session storage
    });
    
    // Clear all localStorage items that start with 'cosmic_'
    Object.keys(localStorage).forEach(key => {
      if (key.startsWith('cosmic_')) {
        localStorage.removeItem(key);
      }
    });
    
    // Clear session storage
    Object.keys(sessionStorage).forEach(key => {
      if (key.startsWith('cosmic_')) {
        sessionStorage.removeItem(key);
      }
    });
    
    console.log('✅ Complete cleanup finished');
    
  } catch (error) {
    console.error('❌ Cleanup error:', error);
  }
};

  /**
   * ✅ ENHANCED: Improved user update with validation
   */
  const updateUser = useCallback((updatedUser: UserProfile): void => {
    try {
      if (!isValidUserProfile(updatedUser)) {
        throw new Error('Invalid user profile data');
      }
      
      const userData = createUserData(updatedUser);
      setUser(userData);
      console.log('✅ User data updated');
    } catch (error) {
      console.error('❌ Error updating user data:', error);
      setError('Failed to update user data');
    }
  }, []);

  /**
   * ✅ ENHANCED: Improved user refresh with error handling
   */
  const refreshUser = async (): Promise<void> => {
    try {
      if (!authService.isAuthenticated()) {
        throw new Error('User is not authenticated');
      }
      
      console.log('🔄 Refreshing user profile...');
      const currentUser = await authService.getUserProfile(true); // Force refresh
      const userData = createUserData(currentUser);
      setUser(userData);
      
      console.log('✅ User profile refreshed successfully');
    } catch (error: any) {
      console.error('❌ Error refreshing user:', error);
      
      if (error.message?.includes('Session expired') || error.message?.includes('Authentication expired')) {
        // Session expired, logout user
        await logout();
        setError('Your session has expired. Please log in again.');
      } else {
        setError('Failed to refresh user data');
      }
      
      throw error;
    }
  };

  // ================ HELPER FUNCTIONS ================

  /**
   * ✅ ENHANCED: Improved device type detection
   */
  // In AuthContext.tsx - update the getDeviceType function

/**
 * ✅ FIXED: Return proper DeviceType union instead of string
 */
const getDeviceType = (): DeviceType => {
  const userAgent = navigator.userAgent.toLowerCase();
  
  if (/android|webos|iphone|ipod|blackberry|iemobile|opera mini/i.test(userAgent)) {
    return 'MOBILE';
  }
  
  if (/ipad|android(?!.*mobile)|tablet|kindle|silk/i.test(userAgent)) {
    return 'TABLET';
  }
  
  if (/smart-tv|smarttv|googletv|appletv|hbbtv|pov_tv|netcast.tv/i.test(userAgent)) {
    return 'TV';
  }
  
  return 'DESKTOP';
};

  /**
   * ✅ NEW: Get timezone with fallback
   */
  const getTimezone = (): string => {
  try {
    return Intl.DateTimeFormat().resolvedOptions().timeZone;
  } catch (error) {
    return 'UTC';
  }
};

  // ================ CONTEXT VALUE ================

  const value: AuthContextType = {
    user,
    isAuthenticated: !!user && authService.isAuthenticated(),
    isLoading,
    isInitialized,
    login,
    register,
    logout,
    updateUser,
    refreshUser,
    error,
    clearError,
  };

  // ================ LOADING STATE ================

  /**
   * ✅ ENHANCED: Better loading state with initialization check
   */
  if (!isInitialized) {
    return (
      <div className="min-h-screen flex items-center justify-center bg-background">
        <div className="flex flex-col items-center space-y-4">
          <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-mystical-mid"></div>
          <p className="text-muted-foreground">Initializing Cosmic Portal...</p>
        </div>
      </div>
    );
  }

  return (
    <AuthContext.Provider value={value}>
      {children}
    </AuthContext.Provider>
  );
};

// ================ ADDITIONAL HOOKS ================

/**
 * ✅ NEW: Hook for handling authentication state changes
 */
export const useAuthState = () => {
  const { isAuthenticated, isLoading, error } = useAuth();
  return { isAuthenticated, isLoading, error };
};

/**
 * ✅ NEW: Hook for user profile operations
 */
export const useUserProfile = () => {
  const { user, updateUser, refreshUser } = useAuth();
  return { user, updateUser, refreshUser };
};

/**
 * ✅ NEW: Hook for authentication actions
 */
export const useAuthActions = () => {
  const { login, register, logout, clearError } = useAuth();
  return { login, register, logout, clearError };
};

// ✅ Don't export types here - they're exported from types/auth.ts
