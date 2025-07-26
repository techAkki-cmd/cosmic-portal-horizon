import { authService } from './authService';

export interface UserProfile {
  id: number;
  username: string;
  email: string;
  firstName: string;
  lastName: string;
  birthDateTime?: string;
  birthLocation?: string;
  birthLatitude?: number;
  birthLongitude?: number;
  timezone?: string;
  role: string;
  createdAt: string;
  lastLogin?: string;
}

export interface BirthData {
  birthDateTime: string;
  birthLocation: string;
  birthLatitude: number;
  birthLongitude: number;
  timezone: string;
}

export interface ProfileUpdateRequest {
  firstName?: string;
  lastName?: string;
  email?: string;
}

class UserService {
  private baseURL = process.env.REACT_APP_API_URL || 'http://localhost:8080/api';

  async getUserProfile(): Promise<UserProfile> {
    const response = await authService.authenticatedRequest('/user/profile');
    
    if (!response.ok) {
      throw new Error('Failed to fetch user profile');
    }
    
    return await response.json();
  }

  async updateProfile(profileData: ProfileUpdateRequest): Promise<UserProfile> {
    const response = await authService.authenticatedRequest('/user/profile', {
      method: 'PUT',
      body: JSON.stringify(profileData),
    });

    if (!response.ok) {
      const errorData = await response.json();
      throw new Error(errorData.message || 'Failed to update profile');
    }

    const updatedUser = await response.json();
    
    // Update local storage
    localStorage.setItem('cosmic_user_data', JSON.stringify(updatedUser));
    
    return updatedUser;
  }

  async getBirthData(): Promise<BirthData> {
    const response = await authService.authenticatedRequest('/user/birth-data');
    
    if (!response.ok) {
      throw new Error('Failed to fetch birth data');
    }
    
    return await response.json();
  }

  async updateBirthData(birthData: BirthData): Promise<UserProfile> {
    const response = await authService.authenticatedRequest('/user/birth-data', {
      method: 'POST',
      body: JSON.stringify(birthData),
    });

    if (!response.ok) {
      const errorData = await response.json();
      throw new Error(errorData.message || 'Failed to update birth data');
    }

    const updatedUser = await response.json();
    
    // Update local storage
    localStorage.setItem('cosmic_user_data', JSON.stringify(updatedUser));
    
    return updatedUser;
  }

  // Location Helper Methods
  async geocodeLocation(address: string): Promise<{ latitude: number; longitude: number; timezone: string }> {
    try {
      // You can integrate with Google Maps Geocoding API or similar
      const response = await fetch(
        `https://api.opencagedata.com/geocode/v1/json?q=${encodeURIComponent(address)}&key=YOUR_API_KEY&limit=1`
      );
      
      if (!response.ok) {
        throw new Error('Geocoding failed');
      }
      
      const data = await response.json();
      
      if (data.results.length === 0) {
        throw new Error('Location not found');
      }
      
      const result = data.results[0];
      return {
        latitude: result.geometry.lat,
        longitude: result.geometry.lng,
        timezone: result.annotations.timezone.name,
      };
    } catch (error) {
      console.error('Geocoding error:', error);
      throw new Error('Failed to get location coordinates');
    }
  }

  validateBirthData(birthData: Partial<BirthData>): string[] {
    const errors: string[] = [];

    if (!birthData.birthDateTime) {
      errors.push('Birth date and time is required');
    }

    if (!birthData.birthLocation) {
      errors.push('Birth location is required');
    }

    if (birthData.birthLatitude === undefined || birthData.birthLatitude === null) {
      errors.push('Birth latitude is required');
    } else if (birthData.birthLatitude < -90 || birthData.birthLatitude > 90) {
      errors.push('Birth latitude must be between -90 and 90');
    }

    if (birthData.birthLongitude === undefined || birthData.birthLongitude === null) {
      errors.push('Birth longitude is required');
    } else if (birthData.birthLongitude < -180 || birthData.birthLongitude > 180) {
      errors.push('Birth longitude must be between -180 and 180');
    }

    if (!birthData.timezone) {
      errors.push('Timezone is required');
    }

    return errors;
  }
}

export const userService = new UserService();
export default userService;
