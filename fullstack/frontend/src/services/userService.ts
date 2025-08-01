// ✅ FIXED: Import types instead of defining them locally
import { authService } from './authService';
import type { 
  UserProfile, 
  BirthProfileData 
} from '../types/auth';
import type {
  ProfileUpdateRequest,
  LocationCoordinates,
  BirthDataValidationResult,
  UserStats,
  UserPreferences
} from '../types/user';

// ================ ENHANCED USER SERVICE CLASS ================

class UserService {
  private readonly baseURL = import.meta.env.VITE_API_URL || 'http://localhost:8080/api';
  private readonly userKey = 'cosmic_user_data';
  private readonly preferencesKey = 'cosmic_user_preferences';

  // ================ PROFILE MANAGEMENT ================

  async getUserProfile(): Promise<UserProfile> {
    try {
      const response = await authService.authenticatedRequest('/users/profile');
      
      if (!response.ok) {
        if (response.status === 404) {
          throw new Error('User profile not found');
        } else if (response.status === 401) {
          throw new Error('Authentication required');
        }
        throw new Error('Failed to fetch user profile');
      }
      
      const profile: UserProfile = await response.json();
      
      // Update local storage
      localStorage.setItem(this.userKey, JSON.stringify(profile));
      
      return profile;
    } catch (error) {
      console.error('Error fetching user profile:', error);
      throw error;
    }
  }

  async updateProfile(profileData: ProfileUpdateRequest): Promise<UserProfile> {
    try {
      // Validate profile data
      const validationErrors = this.validateProfileData(profileData);
      if (validationErrors.length > 0) {
        throw new Error(`Validation failed: ${validationErrors.join(', ')}`);
      }

      const response = await authService.authenticatedRequest('/users/profile', {
        method: 'PUT',
        body: JSON.stringify(profileData),
      });

      if (!response.ok) {
        const errorData = await this.handleErrorResponse(response);
        throw new Error(errorData.message || 'Failed to update profile');
      }

      const updatedUser: UserProfile = await response.json();
      
      // Update local storage
      localStorage.setItem(this.userKey, JSON.stringify(updatedUser));
      
      return updatedUser;
    } catch (error) {
      console.error('Error updating profile:', error);
      throw error;
    }
  }

  async deleteAccount(password: string, reason?: string): Promise<void> {
    try {
      const response = await authService.authenticatedRequest('/users/account', {
        method: 'DELETE',
        body: JSON.stringify({ password, reason }),
      });

      if (!response.ok) {
        const errorData = await this.handleErrorResponse(response);
        throw new Error(errorData.message || 'Failed to delete account');
      }

      // Clear all local data
      this.clearLocalData();
    } catch (error) {
      console.error('Error deleting account:', error);
      throw error;
    }
  }

  // ================ BIRTH DATA MANAGEMENT ================

  async getBirthData(): Promise<BirthProfileData | null> {
    try {
      const response = await authService.authenticatedRequest('/users/birth-data');
      
      if (response.status === 404) {
        return null; // No birth data set yet
      }
      
      if (!response.ok) {
        throw new Error('Failed to fetch birth data');
      }
      
      return await response.json();
    } catch (error) {
      console.error('Error fetching birth data:', error);
      throw error;
    }
  }

  async updateBirthData(birthData: BirthProfileData): Promise<UserProfile> {
    try {
      // Validate birth data
      const validation = this.validateBirthData(birthData);
      if (!validation.isValid) {
        throw new Error(`Birth data validation failed: ${validation.errors.join(', ')}`);
      }

      const response = await authService.authenticatedRequest('/users/birth-profile', {
        method: 'PUT',
        body: JSON.stringify(birthData),
      });

      if (!response.ok) {
        const errorData = await this.handleErrorResponse(response);
        throw new Error(errorData.message || 'Failed to update birth data');
      }

      const updatedUser: UserProfile = await response.json();
      
      // Update local storage
      localStorage.setItem(this.userKey, JSON.stringify(updatedUser));
      
      return updatedUser;
    } catch (error) {
      console.error('Error updating birth data:', error);
      throw error;
    }
  }

  // ================ USER STATISTICS ================

  async getUserStats(): Promise<UserStats> {
    try {
      const response = await authService.authenticatedRequest('/users/stats');
      
      if (!response.ok) {
        throw new Error('Failed to fetch user statistics');
      }
      
      return await response.json();
    } catch (error) {
      console.error('Error fetching user stats:', error);
      // Return default stats if API fails
      return {
        chartsCreated: 0,
        accuracyRate: 0,
        cosmicEnergy: 'Low',
        streakDays: 0,
        totalSessions: 0,
        lastActive: new Date().toISOString(),
        memberSince: new Date().toISOString(),
        subscriptionStatus: 'Free'
      };
    }
  }

  // ================ USER PREFERENCES ================

  async getUserPreferences(): Promise<UserPreferences> {
    try {
      const response = await authService.authenticatedRequest('/users/preferences');
      
      if (!response.ok) {
        throw new Error('Failed to fetch user preferences');
      }
      
      const preferences: UserPreferences = await response.json();
      
      // Cache preferences locally
      localStorage.setItem(this.preferencesKey, JSON.stringify(preferences));
      
      return preferences;
    } catch (error) {
      console.error('Error fetching user preferences:', error);
      // Return cached preferences or defaults
      const cached = localStorage.getItem(this.preferencesKey);
      if (cached) {
        return JSON.parse(cached);
      }
      return this.getDefaultPreferences();
    }
  }

  async updateUserPreferences(preferences: Partial<UserPreferences>): Promise<UserPreferences> {
    try {
      const response = await authService.authenticatedRequest('/users/preferences', {
        method: 'PUT',
        body: JSON.stringify(preferences),
      });

      if (!response.ok) {
        const errorData = await this.handleErrorResponse(response);
        throw new Error(errorData.message || 'Failed to update preferences');
      }

      const updatedPreferences: UserPreferences = await response.json();
      
      // Update local storage
      localStorage.setItem(this.preferencesKey, JSON.stringify(updatedPreferences));
      
      return updatedPreferences;
    } catch (error) {
      console.error('Error updating preferences:', error);
      throw error;
    }
  }

  // ================ LOCATION SERVICES ================

  async geocodeLocation(address: string): Promise<LocationCoordinates> {
    try {
      // First try with your backend geocoding service
      const response = await authService.authenticatedRequest('/users/geocode', {
        method: 'POST',
        body: JSON.stringify({ address }),
      });

      if (response.ok) {
        return await response.json();
      }

      // Fallback to client-side geocoding
      return await this.clientSideGeocode(address);
    } catch (error) {
      console.error('Geocoding error:', error);
      throw new Error('Failed to get location coordinates');
    }
  }

  private async clientSideGeocode(address: string): Promise<LocationCoordinates> {
    try {
      // Using OpenCage Geocoding API as fallback
      const apiKey = import.meta.env.VITE_OPENCAGE_API_KEY;
      if (!apiKey) {
        throw new Error('Geocoding service not configured');
      }

      const response = await fetch(
        `https://api.opencagedata.com/geocode/v1/json?q=${encodeURIComponent(address)}&key=${apiKey}&limit=1&language=en`
      );
      
      if (!response.ok) {
        throw new Error('Geocoding service unavailable');
      }
      
      const data = await response.json();
      
      if (data.results.length === 0) {
        throw new Error('Location not found. Please try a more specific address.');
      }
      
      const result = data.results[0];
      const components = result.components;
      
      return {
        latitude: result.geometry.lat,
        longitude: result.geometry.lng,
        timezone: result.annotations.timezone.name,
        accuracy: result.confidence,
        city: components.city || components.town || components.village,
        state: components.state || components.province,
        country: components.country,
        formattedAddress: result.formatted
      };
    } catch (error) {
      console.error('Client-side geocoding error:', error);
      throw error;
    }
  }

  // ================ VALIDATION METHODS ================

  validateBirthData(birthData: Partial<BirthProfileData>): BirthDataValidationResult {
    const errors: string[] = [];
    const warnings: string[] = [];
    let completionPercentage = 0;
    const totalFields = 5;
    let completedFields = 0;

    // Required validations
    if (!birthData.birthDateTime) {
      errors.push('Birth date and time is required');
    } else {
      completedFields++;
      // Validate date is not in future
      const birthDate = new Date(birthData.birthDateTime);
      if (birthDate > new Date()) {
        errors.push('Birth date cannot be in the future');
      }
    }

    if (!birthData.birthLocation) {
      errors.push('Birth location is required');
    } else {
      completedFields++;
    }

    if (birthData.birthLatitude === undefined || birthData.birthLatitude === null) {
      errors.push('Birth latitude is required');
    } else {
      completedFields++;
      if (birthData.birthLatitude < -90 || birthData.birthLatitude > 90) {
        errors.push('Birth latitude must be between -90 and 90 degrees');
      }
    }

    if (birthData.birthLongitude === undefined || birthData.birthLongitude === null) {
      errors.push('Birth longitude is required');
    } else {
      completedFields++;
      if (birthData.birthLongitude < -180 || birthData.birthLongitude > 180) {
        errors.push('Birth longitude must be between -180 and 180 degrees');
      }
    }

    if (!birthData.timezone) {
      errors.push('Timezone is required');
    } else {
      completedFields++;
      // Validate timezone format
      try {
        Intl.DateTimeFormat(undefined, { timeZone: birthData.timezone });
      } catch (e) {
        errors.push('Invalid timezone format');
      }
    }

    completionPercentage = (completedFields / totalFields) * 100;

    return {
      isValid: errors.length === 0,
      errors,
      warnings,
      completionPercentage
    };
  }

  validateProfileData(profileData: ProfileUpdateRequest): string[] {
    const errors: string[] = [];

    if (profileData.email) {
      const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
      if (!emailRegex.test(profileData.email)) {
        errors.push('Invalid email format');
      }
    }

    if (profileData.firstName && profileData.firstName.length < 1) {
      errors.push('First name cannot be empty');
    }

    if (profileData.firstName && profileData.firstName.length > 50) {
      errors.push('First name cannot exceed 50 characters');
    }

    if (profileData.lastName && profileData.lastName.length > 50) {
      errors.push('Last name cannot exceed 50 characters');
    }

    if (profileData.phoneNumber) {
      const phoneRegex = /^[+]?[\d\s\-\(\)]{10,15}$/;
      if (!phoneRegex.test(profileData.phoneNumber)) {
        errors.push('Invalid phone number format');
      }
    }

    return errors;
  }

  // ================ UTILITY METHODS ================

  private getDefaultPreferences(): UserPreferences {
    return {
      theme: 'system',
      language: 'en',
      timezone: Intl.DateTimeFormat().resolvedOptions().timeZone,
      ayanamsa: 'Lahiri',
      chartStyle: 'Traditional',
      notifications: {
        email: true,
        push: false,
        dailyHoroscope: true,
        transitAlerts: false,
        marketing: false
      },
      privacy: {
        profileVisibility: 'private',
        shareReadings: false,
        allowContactFromAstrologers: false
      }
    };
  }

  private async handleErrorResponse(response: Response): Promise<any> {
    try {
      const contentType = response.headers.get('content-type');
      if (contentType && contentType.includes('application/json')) {
        return await response.json();
      } else {
        const text = await response.text();
        return { message: text || 'An error occurred' };
      }
    } catch (error) {
      return { message: 'Failed to parse error response' };
    }
  }

  private clearLocalData(): void {
    localStorage.removeItem(this.userKey);
    localStorage.removeItem(this.preferencesKey);
    localStorage.removeItem('cosmic_auth_token');
    localStorage.removeItem('cosmic_refresh_token');
  }

  // ================ PROFILE COMPLETION HELPERS ================

  calculateProfileCompletion(user: UserProfile): number {
    const fields = [
      user.firstName,
      user.lastName,
      user.email,
      user.birthDateTime,
      user.birthLocation,
      user.birthLatitude,
      user.birthLongitude,
      user.timezone
    ];

    const completedFields = fields.filter(field => 
      field !== null && field !== undefined && field !== ''
    ).length;

    return Math.round((completedFields / fields.length) * 100);
  }

  getIncompleteFields(user: UserProfile): string[] {
    const incompleteFields: string[] = [];

    if (!user.firstName) incompleteFields.push('First Name');
    if (!user.lastName) incompleteFields.push('Last Name');
    if (!user.email) incompleteFields.push('Email');
    if (!user.birthDateTime) incompleteFields.push('Birth Date & Time');
    if (!user.birthLocation) incompleteFields.push('Birth Location');
    if (!user.birthLatitude) incompleteFields.push('Birth Coordinates');
    if (!user.timezone) incompleteFields.push('Timezone');

    return incompleteFields;
  }

  // ================ EXPORT PROFILE DATA ================

  async exportUserData(): Promise<Blob> {
    try {
      const [profile, birthData, preferences, stats] = await Promise.all([
        this.getUserProfile(),
        this.getBirthData(),
        this.getUserPreferences(),
        this.getUserStats()
      ]);

      const exportData = {
        profile,
        birthData,
        preferences,
        stats,
        exportDate: new Date().toISOString(),
        version: '1.0'
      };

      return new Blob([JSON.stringify(exportData, null, 2)], {
        type: 'application/json'
      });
    } catch (error) {
      console.error('Error exporting user data:', error);
      throw new Error('Failed to export user data');
    }
  }
}

// ================ EXPORTS ================

export const userService = new UserService();
export default userService;

// ✅ FIXED: Don't export types here - they're exported from types/user.ts
