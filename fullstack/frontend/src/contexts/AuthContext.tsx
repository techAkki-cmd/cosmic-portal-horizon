import React, { createContext, useContext, useState, useEffect, ReactNode } from 'react';
import { authService } from '../services/authService';

// Add this to your AuthContext.tsx at the top after imports
console.log('🔍 Debug - authService import:', authService);
console.log('🔍 Debug - authService type:', typeof authService);
console.log('🔍 Debug - authService.login type:', typeof authService?.login);
console.log('🔍 Debug - authService methods:', Object.getOwnPropertyNames(authService));

// If authService is undefined or doesn't have login method, you'll see it here

// ✅ Define User interface locally (not imported from authService)
interface User {
  id: number;
  username: string;
  email: string;
  firstName?: string;
  lastName?: string;
  birthDateTime?: string;
  birthLocation?: string;
  birthLatitude?: number;
  birthLongitude?: number;
  timezone?: string;
  role?: string;
  createdAt?: string;
  lastLogin?: string;
  name?: string; // Computed display name
}

// ✅ Fixed interface to match actual implementation
interface AuthContextType {
  user: User | null;
  isAuthenticated: boolean;
  isLoading: boolean;
  login: (username: string, password: string) => Promise<void>; // ✅ Changed from email to username
  register: (data: {
    username: string;     // ✅ Changed from name to username
    email: string;
    password: string;
    firstName?: string;   // ✅ Added firstName
    lastName?: string;    // ✅ Added lastName
    birthDateTime?: string;
    birthLocation?: string;
    birthLatitude?: number;
    birthLongitude?: number;
    timezone?: string;
  }) => Promise<void>;
  logout: () => Promise<void>;
  updateUser: (user: User) => void;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (context === undefined) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
};

interface AuthProviderProps {
  children: ReactNode;
}

export const AuthProvider: React.FC<AuthProviderProps> = ({ children }) => {
  const [user, setUser] = useState<User | null>(null);
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    const initializeAuth = async () => {
      try {
        if (authService.isAuthenticated()) {
          console.log('🔄 Initializing auth - token found');
          const currentUser = await authService.getUserProfile(); // ✅ Use getUserProfile instead of getCurrentUser
          
          // Create display name
          const displayName = currentUser.firstName 
            ? `${currentUser.firstName} ${currentUser.lastName || ''}`.trim()
            : currentUser.username;
          
          const userData: User = {
            ...currentUser,
            name: displayName
          };
          
          setUser(userData);
          console.log('✅ Auth initialized successfully for user:', userData.username);
        } else {
          console.log('ℹ️ No valid token found during auth initialization');
        }
      } catch (error) {
        console.error('❌ Auth initialization error:', error);
        // Clear invalid auth data
        await authService.logout();
      } finally {
        setIsLoading(false);
      }
    };

    initializeAuth();
  }, []);

  // ✅ FIXED: Login function now uses username parameter
  const login = async (username: string, password: string) => {
    console.log('🔐 AuthContext login called with username:', username);
    setIsLoading(true);
    
    try {
      // ✅ Validate input
      if (!username || !password) {
        throw new Error('Username and password are required');
      }

      // ✅ Call authService with username (not email)
      const response = await authService.login({ username, password });
      console.log('✅ AuthService login successful');
      
      // Fetch user profile after successful login
      const userProfile = await authService.getUserProfile();
      
      // Create display name
      const displayName = userProfile.firstName 
        ? `${userProfile.firstName} ${userProfile.lastName || ''}`.trim()
        : userProfile.username;
      
      const userData: User = {
        ...userProfile,
        name: displayName
      };
      
      setUser(userData);
      console.log('✅ User data set in AuthContext:', { username: userData.username, name: userData.name });
      
    } catch (error) {
      console.error('❌ AuthContext login error:', error);
      setUser(null);
      throw error;
    } finally {
      setIsLoading(false);
    }
  };

  // ✅ FIXED: Register function matches interface and implementation
  const register = async (data: {
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
  }) => {
    console.log('📝 AuthContext.register called with:', {
      username: data.username,
      email: data.email,
      password: data.password ? '***' : 'MISSING',
      firstName: data.firstName,
      lastName: data.lastName
    });
    
    setIsLoading(true);
    
    try {
      // ✅ Validate required data
      if (!data.username || !data.email || !data.password) {
        throw new Error('Username, email, and password are required in AuthContext');
      }

      if (data.username.trim() === '' || data.email.trim() === '' || data.password.trim() === '') {
        throw new Error('Username, email, and password cannot be empty');
      }

      // ✅ Call authService signup with exact data structure
      const response = await authService.signup({
        username: data.username.trim(),
        email: data.email.trim(),
        password: data.password.trim(),
        firstName: data.firstName?.trim(),
        lastName: data.lastName?.trim(),
        birthDateTime: data.birthDateTime,
        birthLocation: data.birthLocation?.trim(),
        birthLatitude: data.birthLatitude,
        birthLongitude: data.birthLongitude,
        timezone: data.timezone
      });
      
      console.log('✅ AuthService.signup successful:', response);
      
      // ✅ FIXED: After successful registration, login with username
      await login(data.username, data.password);
      
    } catch (error) {
      console.error('❌ AuthContext.register error:', error);
      throw error;
    } finally {
      setIsLoading(false);
    }
  };

  const logout = async () => {
    console.log('🚪 AuthContext logout called');
    setIsLoading(true);
    
    try {
      await authService.logout();
      setUser(null);
      console.log('✅ Logout successful');
    } catch (error) {
      console.error('❌ Logout error:', error);
      setUser(null); // Clear user even if logout API fails
    } finally {
      setIsLoading(false);
    }
  };

  const updateUser = (updatedUser: User) => {
    console.log('🔄 Updating user data:', updatedUser.username);
    
    // Create display name
    const displayName = updatedUser.firstName 
      ? `${updatedUser.firstName} ${updatedUser.lastName || ''}`.trim()
      : updatedUser.username;
    
    const userData: User = {
      ...updatedUser,
      name: displayName
    };
    
    setUser(userData);
    localStorage.setItem('cosmic_user_data', JSON.stringify(userData));
  };

  const value: AuthContextType = {
    user,
    isAuthenticated: !!user,
    isLoading,
    login,
    register,
    logout,
    updateUser,
  };

  // Show loading spinner during auth initialization
  if (isLoading && user === null) {
    return (
      <div className="min-h-screen flex items-center justify-center bg-background">
        <div className="flex flex-col items-center space-y-4">
          <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-mystical-mid"></div>
          <p className="text-muted-foreground">Initializing...</p>
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
