// ================ USER SERVICE TYPES ================

export interface ProfileUpdateRequest {
  firstName?: string;
  lastName?: string;
  email?: string;
  phoneNumber?: string;
  timezone?: string;
  preferredLanguage?: string;
  preferredAyanamsa?: string;
  timeFormat?: string;
  dateFormat?: string;
  emailNotifications?: boolean;
  dailyHoroscope?: boolean;
  transitAlerts?: boolean;
  marketingEmails?: boolean;
}

export interface LocationCoordinates {
  latitude: number;
  longitude: number;
  timezone: string;
  accuracy?: number;
  city?: string;
  state?: string;
  country?: string;
  formattedAddress?: string;
}

export interface BirthDataValidationResult {
  isValid: boolean;
  errors: string[];
  warnings: string[];
  completionPercentage: number;
}

export interface UserStats {
  chartsCreated: number;
  accuracyRate: number;
  cosmicEnergy: 'Low' | 'Medium' | 'High';
  streakDays: number;
  totalSessions: number;
  lastActive: string;
  memberSince: string;
  subscriptionStatus: 'Free' | 'Premium' | 'Enterprise';
}

export interface UserPreferences {
  theme: 'light' | 'dark' | 'system';
  language: string;
  timezone: string;
  ayanamsa: string;
  chartStyle: 'Traditional' | 'Modern' | 'Minimalist';
  notifications: {
    email: boolean;
    push: boolean;
    dailyHoroscope: boolean;
    transitAlerts: boolean;
    marketing: boolean;
  };
  privacy: {
    profileVisibility: 'public' | 'private' | 'friends';
    shareReadings: boolean;
    allowContactFromAstrologers: boolean;
  };
}
