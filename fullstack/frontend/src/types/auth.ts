// ================ BASE USER TYPES ================

export interface UserProfile {
  // Basic Identity - ✅ FIXED: Allow flexible ID type
  id: string | number;
  username: string;
  email: string;
  
  // Personal Information
  firstName?: string;
  lastName?: string;
  fullName?: string;
  displayName?: string;
  phoneNumber?: string;
  gender?: string;
  occupation?: string;
  nationality?: string;
  
  // Profile Status
  role: 'CLIENT' | 'ASTROLOGER' | 'ADMIN';
  enabled: boolean;
  emailVerified: boolean;
  profileCompletionPercentage: number;
  
  // Astrology Data
  hasCompleteBirthData?: boolean;
  hasGeneratedChart?: boolean;
  sunSign?: string;
  moonSign?: string;
  risingSign?: string;
  dominantElement?: string;
  moonNakshatra?: string;
  moonPada?: number;
  birthDateTime?: string;
  birthLocation?: string;
  birthLatitude?: number;
  birthLongitude?: number;
  timezone?: string;
  
  // Subscription & Activity
  subscriptionType?: string;
  subscriptionActive?: boolean;
  subscriptionEndDate?: string;
  chartsGenerated?: number;
  totalLogins?: number;
  loginStreak?: number;
  
  // Timestamps
  memberSince?: string;
  createdAt?: string;
  updatedAt?: string;
  lastLogin?: string;
  lastActiveDate?: string;
  
  // Profile Picture
  profilePictureUrl?: string;
  
  // Additional Properties
  [key: string]: any;
}

// ✅ FIXED: Device type union - make it consistent
export type DeviceType = 'MOBILE' | 'TABLET' | 'DESKTOP' | 'TV';

// ✅ FIXED: Export User as type alias (SINGLE EXPORT ONLY)
export type User = UserProfile;

// ✅ NEW: Generic API response wrapper
export interface ApiResponse<T = any> {
  success: boolean;
  code: string;
  message: string;
  timestamp: string;
  data?: T;
  errors?: any;
}

// ✅ NEW: Birth time source type (MUST BE DEFINED BEFORE BirthProfileData)
export type BirthTimeSource = 
  | 'BIRTH_CERTIFICATE' 
  | 'HOSPITAL_RECORD' 
  | 'FAMILY_KNOWLEDGE' 
  | 'ESTIMATED'
  | 'USER_PROVIDED';

// ================ AUTHENTICATION REQUEST TYPES ================

export interface LoginCredentials {
  // Required authentication fields
  username: string;
  password: string;
  
  // Optional email field (for email-based login)
  email?: string;
  
  // ✅ FIXED: Use the DeviceType union
  userAgent?: string;
  deviceType?: DeviceType;
  timezone?: string;
  
  // Login Options
  rememberMe?: boolean;
  
  // Security & Tracking
  ipAddress?: string;
  sessionId?: string;
  
  // Additional metadata
  [key: string]: any;
}

export interface SignupData {
  // Required registration fields
  username: string;
  email: string;
  password: string;
  firstName: string;
  lastName?: string;
  
  // Phone and Personal
  phoneNumber?: string;
  gender?: 'Male' | 'Female' | 'Other' | 'Prefer not to say';
  occupation?: string;
  nationality?: string;
  
  // Legal Agreements (REQUIRED)
  agreeToTerms: boolean;
  agreeToPrivacyPolicy: boolean;
  ageConfirmation: boolean;
  
  // Agreement Version Tracking
  acceptedTermsVersion?: string;
  acceptedPrivacyVersion?: string;
  
  // Marketing & Communication
  subscribeToNewsletter?: boolean;
  allowMarketingEmails?: boolean;
  
  // ✅ FIXED: Use the DeviceType union
  userAgent?: string;
  deviceType?: DeviceType;
  timezone?: string;
  
  // Registration Tracking
  registrationSource?: 'ORGANIC' | 'REFERRAL' | 'SOCIAL' | 'ADVERTISEMENT';
  referralCode?: string;
  utmSource?: string;
  utmMedium?: string;
  utmCampaign?: string;
  
  // Birth Information (Optional during signup)
  birthDateTime?: string;
  birthLocation?: string;
  birthLatitude?: number;
  birthLongitude?: number;
  
  // Additional metadata
  [key: string]: any;
}

// ✅ FIXED: Single BirthProfileData interface with correct type
export interface BirthProfileData {
  // Required Birth Information
  birthDateTime: string;
  birthLocation: string;
  birthLatitude: number | null;
  birthLongitude: number | null;
  
  // Optional Birth Details
  timezone?: string;
  birthCity?: string;
  birthState?: string;
  birthCountry?: string;
  
  // Accuracy Information
  isTimeAccurate?: boolean;
  timeSource?: BirthTimeSource; // ✅ FIXED: Uses the proper type with 'USER_PROVIDED'
  
  // Additional metadata
  [key: string]: any;
}

// ================ AUTHENTICATION RESPONSE TYPES ================

export interface AuthResponse {
  // Direct format (for backward compatibility)
  token?: string;
  refreshToken?: string;
  user?: UserProfile;
  
  // ✅ ENHANCED: Wrapped format (current backend structure)
  success?: boolean;
  code?: string;
  message?: string;
  timestamp?: string;
  data?: {
    token: string;
    refreshToken?: string;
    user?: UserProfile;
    tokenType?: string;
    expiresIn?: number;
  };
  
  // Token Information
  tokenType?: 'Bearer';
  expiresIn?: number;
  expiresAt?: string;
  loginTimestamp?: string;
  sessionId?: string;
  
  [key: string]: any;
}

export interface SignupResponse {
  message: string;
  user?: UserProfile;
  requiresEmailVerification?: boolean;
  verificationEmailSent?: boolean;
  userId?: number;
  
  // Additional response data
  [key: string]: any;
}

// ================ API ERROR TYPES ================

export interface ApiError {
  message: string;
  error?: string;
  status?: number;
  code?: string;
  timestamp?: string;
  path?: string;
  details?: Record<string, any>;
}

export interface ValidationError extends ApiError {
  field?: string;
  validationErrors?: Array<{
    field: string;
    message: string;
    rejectedValue?: any;
  }>;
}

// ================ USER PREFERENCES TYPES ================

export interface UserPreferences {
  // Display Preferences
  preferredLanguage: string;
  timeFormat: '12h' | '24h';
  dateFormat: string;
  timezone: string;
  
  // Notification Preferences
  emailNotifications: boolean;
  dailyHoroscope: boolean;
  transitAlerts: boolean;
  pushNotifications?: boolean;
  smsNotifications?: boolean;
  
  // Privacy Preferences
  profileVisibility: 'PUBLIC' | 'PRIVATE' | 'FRIENDS_ONLY';
  allowDataAnalytics?: boolean;
  
  // Astrology Preferences
  preferredAyanamsa?: string;
  preferredChartStyle?: string;
  defaultChartType?: string;
  
  // Additional preferences
  [key: string]: any;
}

// ================ BIRTH DATA RESPONSE TYPES ================

export interface BirthDataResponse {
  birthDateTime?: string;
  birthLocation?: string;
  birthLatitude?: number;
  birthLongitude?: number;
  timezone?: string;
  birthCity?: string;
  birthState?: string;
  birthCountry?: string;
  dataComplete: boolean;
  lastUpdated?: string;
  
  // Validation Information
  coordinatesVerified?: boolean;
  timezoneDetected?: boolean;
  
  // Additional data
  [key: string]: any;
}

// ================ USER PREFERENCES REQUEST/RESPONSE TYPES ================

export interface UserPreferencesRequest {
  preferredLanguage?: string;
  timeFormat?: '12h' | '24h';
  dateFormat?: string;
  timezone?: string;
  emailNotifications?: boolean;
  dailyHoroscope?: boolean;
  transitAlerts?: boolean;
  
  // Additional preferences
  [key: string]: any;
}

export interface UserPreferencesResponse extends UserPreferences {
  lastUpdated?: string;
  userId?: number;
}

// ================ ACCOUNT MANAGEMENT TYPES ================

export interface AccountStatusResponse {
  username: string;
  email: string;
  enabled: boolean;
  emailVerified: boolean;
  role: string;
  memberSince?: string;
  lastLogin?: string;
  lastActiveDate?: string;
  profileCompletionPercentage: number;
  chartsGenerated?: number;
  loginStreak?: number;
  totalLogins?: number;
  subscriptionType?: string;
  subscriptionActive: boolean;
  subscriptionEndDate?: string;
  creditsRemaining?: number;
  accountLocked: boolean;
  daysSinceLastLogin?: number;
  
  // Additional status information
  [key: string]: any;
}

export interface AccountSettingsRequest {
  emailNotifications?: boolean;
  dailyHoroscope?: boolean;
  transitAlerts?: boolean;
  
  // Additional settings
  [key: string]: any;
}

export interface AccountSettingsResponse {
  emailNotifications: boolean;
  dailyHoroscope: boolean;
  transitAlerts: boolean;
  lastUpdated?: string;
  
  // Additional settings
  [key: string]: any;
}

// ================ PROFILE MANAGEMENT TYPES ================

export interface UserProfileRequest {
  firstName?: string;
  lastName?: string;
  phoneNumber?: string;
  gender?: string;
  occupation?: string;
  nationality?: string;
  preferredLanguage?: string;
  timeFormat?: string;
  dateFormat?: string;
  
  // Additional profile fields
  [key: string]: any;
}

export interface UserProfileResponse extends UserProfile {
  // Additional response-specific fields
  lastUpdated?: string;
  profileHealth?: {
    completionPercentage: number;
    missingFields: string[];
    recommendations: string[];
  };
}

// ================ PROFILE PICTURE TYPES ================

export interface ProfilePictureResponse {
  profilePictureUrl: string;
  filename: string;
  fileSize: number;
  uploadedAt: string;
  
  // Additional metadata
  [key: string]: any;
}

// ================ CHART HISTORY TYPES ================

export interface ChartHistoryResponse {
  username: string;
  totalChartsGenerated?: number;
  lastChartGenerated?: string;
  chartHistory: Array<{
    id?: number;
    type: string;
    calculatedAt: string;
    accuracy?: string;
    ayanamsa?: string;
    [key: string]: any;
  }>;
  historyCount: number;
  
  // Pagination
  page?: number;
  size?: number;
  totalElements?: number;
}

// ================ PROFILE COMPLETION TYPES ================

export interface ProfileCompletionResponse {
  username: string;
  completionPercentage: number;
  missingFields: string[];
  completedFields: string[];
  isComplete: boolean;
  
  // Recommendations
  nextSteps?: string[];
  priority?: 'HIGH' | 'MEDIUM' | 'LOW';
}

// ================ UTILITY TYPES ================

export interface MessageResponse {
  message: string;
  status?: 'success' | 'error' | 'warning' | 'info';
  timestamp?: number;
  
  // Additional metadata
  [key: string]: any;
}

export interface PaginationParams {
  page?: number;
  size?: number;
  sort?: string;
  direction?: 'ASC' | 'DESC';
}

export interface PaginatedResponse<T> {
  content: T[];
  page: number;
  size: number;
  totalElements: number;
  totalPages: number;
  first: boolean;
  last: boolean;
  empty: boolean;
}

// ================ PASSWORD MANAGEMENT TYPES ================

export interface ChangePasswordRequest {
  currentPassword: string;
  newPassword: string;
  confirmPassword?: string;
}

export interface ForgotPasswordRequest {
  email: string;
}

export interface ResetPasswordRequest {
  token: string;
  newPassword: string;
  confirmPassword?: string;
}

export interface EmailVerificationRequest {
  token: string;
}

// ================ TOKEN TYPES ================

export interface TokenPayload {
  sub: string;
  username: string;
  email?: string;
  role: string;
  exp: number;
  iat: number;
  authorities?: string[];
  
  // Additional claims
  [key: string]: any;
}

export interface RefreshTokenRequest {
  refreshToken: string;
}

export interface RefreshTokenResponse {
  token: string;
  refreshToken?: string;
  expiresIn?: number;
  tokenType?: 'Bearer';
}

// ================ FORM VALIDATION TYPES ================

export interface FieldError {
  field: string;
  message: string;
  code?: string;
  rejectedValue?: any;
}

export interface FormValidationResult {
  valid: boolean;
  errors: FieldError[];
  warnings?: FieldError[];
}

// ================ CLIENT INFORMATION TYPES ================

export interface ClientInfo {
  userAgent: string;
  deviceType: DeviceType; // ✅ FIXED: Use the DeviceType union
  timezone: string;
  ipAddress?: string;
  sessionId?: string;
  language?: string;
  platform?: string;
  browser?: string;
  os?: string;
}

// ================ FINAL EXPORTS ================
// ✅ FIXED: No duplicate exports - User is already exported above
// Note: User type is exported above as: export type User = UserProfile;
