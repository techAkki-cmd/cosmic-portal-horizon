// src/types/astrology.ts

// ================ EXISTING TYPES ================
// (Keep your existing types here)

export interface PersonalizedMessage {
  message: string;
  transitInfluence: string;
  recommendation: string;
  intensity: number;
  dominantPlanet: string;
  luckyColor: string;
  bestTimeOfDay: string;
  moonPhase: string;
}

export interface BirthChart {
  id: string;
  userId: string;
  chartType: 'Natal' | 'Transit' | 'Prashna' | 'Muhurta';
  createdAt: string;
  updatedAt: string;
  name: string;
  birthData: BirthData;
  chartData: {
    sunSign: string;
    moonSign: string;
    risingSign: string;
    dominantElement: string;
    planetaryPositions: { [planetName: string]: number };
    houses: HousePosition[];
    aspects: AspectDetail[];
    nakshatras: { [planetName: string]: NakshatraDetail };
  };
  insights?: ChartInsight[];
  isPublic: boolean;
  tags?: string[];
  notes?: string;
}

export interface TransitForecast {
  id: string;
  date: string;
  planet: string;
  transitType: 'ingress' | 'retrograde' | 'direct' | 'conjunction' | 'opposition';
  fromSign?: string;
  toSign: string;
  exactTime: string;
  significance: 'major' | 'moderate' | 'minor';
  description: string;
  affects: string[];
  duration?: string;
  nextSimilarTransit?: string;
  remedialSuggestions?: string[];
}

export interface DashaData {
  system: 'Vimshottari' | 'Ashtottari' | 'Yogini' | 'Chara';
  birthStar: string;
  balanceAtBirth: {
    planet: string;
    years: number;
    months: number;
    days: number;
  };
  currentPeriods: {
    mahadasha: DashaPeriodDetail;
    antardasha: DashaPeriodDetail;
    pratyantardasha: DashaPeriodDetail;
    sookshma?: DashaPeriodDetail;
    prana?: DashaPeriodDetail;
  };
  upcomingTransitions: DashaTransition[];
  lifeTimeline: DashaPeriodDetail[];
  significantPeriods: {
    favorable: DashaPeriodDetail[];
    challenging: DashaPeriodDetail[];
    transformative: DashaPeriodDetail[];
  };
  remedies: {
    [planet: string]: string[];
  };
}

export interface LifeAreaInfluence {
  title: string;
  rating: number;
  insight: string;
  icon: string;
  gradient: string;
}

export interface AstrologyUserStats {
  chartsCreated: number;
  accuracyRate: number;
  cosmicEnergy: string;
  streakDays: number;
  totalReadings: number;
  favoriteChartType: string;
  mostActiveTimeOfDay: string;
  averageSessionDuration: number;
  totalPredictions: number;
  correctPredictions: number;
  lastChartGenerated?: Date;
}

// ================ NEW ENHANCED TYPES ================

/**
 * 🕉️ YOGA ANALYSIS RESPONSE
 * Complete response for Vedic Yoga analysis
 */
export interface YogaAnalysisResponse {
  totalYogas: number;
  rajaYogas: YogaDetail[];
  dhanaYogas: YogaDetail[];
  spiritualYogas: YogaDetail[];
  mahapurushaYogas: YogaDetail[];
  challengingYogas: YogaDetail[];
  yogaStrength: number;
  topYogas: YogaDetail[];
  overallYogaAssessment?: string;
}

/**
 * Individual Yoga Details
 */
export interface YogaDetail {
  yogaName: string;
  yogaType: string;
  description: string;
  rarity: number;
  strength: string;
  manifestation: string;
  remedy?: string;
  instructions?: string;
  effectiveness?: number;
  priority?: number;
  isVeryRare?: boolean;
  category?: string;
}

/**
 * 📅 DASHA ANALYSIS RESPONSE
 * Complete response for Vimshottari Dasha analysis
 */
export interface DashaAnalysisResponse {
  currentMahadasha: string;
  currentAntardasha: string;
  currentPratyantardasha?: string;
  mahadashaRemaining: string;
  dashaInterpretation: string;
  upcomingPeriods: DashaPeriod[];
  dashaRemedies: string[];
  favorablePeriods: string[];
  intensity?: number;
  dashaStrength?: string;
}

/**
 * Individual Dasha Period Details
 */
export interface DashaPeriod {
  period: string;
  planet: string;
  startDate?: string;
  endDate?: string;
  duration: string;
  theme: string;
  description?: string;
  color?: string;
}

/**
 * 💎 REMEDIAL RECOMMENDATIONS RESPONSE
 * Complete response for personalized astrological remedies
 */
export interface RemedialRecommendationsResponse {
  totalRemedies: number;
  gemstoneRemedies: RemedyDetail[];
  mantraRemedies: RemedyDetail[];
  healthRemedies: RemedyDetail[];
  careerRemedies: RemedyDetail[];
  relationshipRemedies: RemedyDetail[];
  lifestyleRemedies: RemedyDetail[];
  priorityRemedies: RemedyDetail[];
  overallGuidance: string;
}

/**
 * Individual Remedy Details
 */
export interface RemedyDetail {
  remedy: string;
  category: string;
  instructions: string;
  description?: string;
  effectiveness: number;
  priority: number;
  duration?: string;
  specialNote?: string;
}

/**
 * 🌍 TRANSIT RESPONSE
 * Individual planetary transit information
 */
export interface TransitResponse {
  planet: string;
  position: number;
  sign: string;
  nakshatra: string;
  pada: number;
  nakshatraLord?: string;
  influence?: string;
  aspectingPlanets?: string[];
  transitType?: 'benefic' | 'malefic' | 'neutral';
  significantAspects?: string[];
}

/**
 * 🎯 BIRTH CHART RESPONSE
 * Complete birth chart calculation response
 */
export interface BirthChartResponse {
  sunSign: string;
  moonSign: string;
  risingSign: string;
  dominantElement: string;
  planetaryPositions: { [planetName: string]: number };
  calculatedAt: string;
  ayanamsa?: number;
  system: string;
  houses?: HousePosition[];
  aspects?: AspectDetail[];
  nakshatras?: { [planetName: string]: NakshatraDetail };
}

/**
 * House Position Details
 */
export interface HousePosition {
  houseNumber: number;
  sign: string;
  lord: string;
  planetsInHouse: string[];
  significance: string[];
}

/**
 * Aspect Details
 */
export interface AspectDetail {
  fromPlanet: string;
  toPlanet: string;
  aspectType: string;
  orb: number;
  strength: 'strong' | 'medium' | 'weak';
  influence: 'harmonious' | 'challenging' | 'neutral';
}

/**
 * Nakshatra Details
 */
export interface NakshatraDetail {
  nakshatra: string;
  nakshatraNumber: number;
  pada: number;
  nakshatraLord: string;
  deity: string;
  symbol: string;
  characteristics: string[];
}

/**
 * 🌟 COMPLETE ANALYSIS RESPONSE
 * Comprehensive response containing all analysis types
 */
export interface CompleteAnalysisResponse {
  personalizedMessage: PersonalizedMessage;
  birthChart: BirthChartResponse;
  yogaAnalysis: YogaAnalysisResponse;
  dashaAnalysis: DashaAnalysisResponse;
  remedialRecommendations: RemedialRecommendationsResponse;
  currentTransits: TransitResponse[];
  lifeAreaInfluences: LifeAreaInfluence[];
  userStats: AstrologyUserStats;
  analysisTimestamp: string;
  analysisId?: string;
}

/**
 * Birth Data Input
 */
export interface BirthData {
  birthDateTime: Date;
  birthLocation: string;
  birthLatitude: number;
  birthLongitude: number;
  timezone: string;
}

// ================ MISSING TYPES FROM YOUR CURRENT FILE ================

/**
 * 📊 DASHA PERIOD DETAIL - Referenced in DashaData
 */
export interface DashaPeriodDetail {
  planet: string;
  startDate: string;
  endDate: string;
  duration: {
    years: number;
    months: number;
    days: number;
  };
  lordStrength: 'strong' | 'moderate' | 'weak';
  effects: string[];
  lifeAreas: string[];
  favorableTimes?: string[];
  challengingTimes?: string[];
}

/**
 * 🔄 DASHA TRANSITION - Referenced in DashaData
 */
export interface DashaTransition {
  date: string;
  type: 'mahadasha' | 'antardasha' | 'pratyantardasha';
  from: string;
  to: string;
  significance: string;
  preparation: string[];
  remedies: string[];
}

/**
 * 💕 COMPATIBILITY RESULT - Relationship analysis
 */
export interface CompatibilityResult {
  id: string;
  analysisType: 'Basic' | 'Detailed' | 'Kulakoota';
  calculatedAt: string;
  person1: {
    name: string;
    birthData: BirthProfileData;
    chartSummary: ChartSummary;
  };
  person2: {
    name: string;
    birthData: BirthProfileData;
    chartSummary: ChartSummary;
  };
  overallScore: number;
  overallRating: 'Excellent' | 'Very Good' | 'Good' | 'Average' | 'Below Average' | 'Poor';
  compatibility: {
    emotional: CompatibilityAspect;
    mental: CompatibilityAspect;
    physical: CompatibilityAspect;
    spiritual: CompatibilityAspect;
    financial: CompatibilityAspect;
    communication: CompatibilityAspect;
  };
  ashtakoota?: AshtakootaAnalysis;
  strengths: string[];
  challenges: string[];
  recommendations: string[];
  favorableTimeForMarriage?: string[];
  avoidTimeForMarriage?: string[];
  additionalInsights: string[];
}

/**
 * Individual Compatibility Aspect
 */
export interface CompatibilityAspect {
  score: number;
  maxScore: number;
  percentage: number;
  rating: 'Excellent' | 'Very Good' | 'Good' | 'Average' | 'Below Average' | 'Poor';
  description: string;
  factors: string[];
}

/**
 * Ashtakoota Analysis for Detailed Compatibility
 */
export interface AshtakootaAnalysis {
  totalScore: number;
  maxScore: 36;
  koota: {
    varna: { score: number; max: 1; description: string };
    vashya: { score: number; max: 2; description: string };
    tara: { score: number; max: 3; description: string };
    yoni: { score: number; max: 4; description: string };
    graha: { score: number; max: 5; description: string };
    gana: { score: number; max: 6; description: string };
    rashi: { score: number; max: 7; description: string };
    nadi: { score: number; max: 8; description: string };
  };
  dosha: {
    mangal: { present: boolean; severity: string; remedies?: string[] };
    nadi: { present: boolean; severity: string; remedies?: string[] };
    bhakoot: { present: boolean; severity: string; remedies?: string[] };
  };
  verdict: string;
}

/**
 * ⏰ MUHURTA RESULT - Auspicious timing
 */
export interface MuhurtaResult {
  id: string;
  purpose: string;
  date: string;
  timeRange: {
    start: string;
    end: string;
    timezone: string;
  };
  quality: 'Excellent' | 'Very Good' | 'Good' | 'Average' | 'Avoid';
  score: number;
  factors: {
    tithi: { name: string; quality: string; score: number };
    vara: { name: string; quality: string; score: number };
    nakshatra: { name: string; quality: string; score: number };
    yoga: { name: string; quality: string; score: number };
    karana: { name: string; quality: string; score: number };
    rahu: { period: string; avoid: boolean };
    gulika: { period: string; avoid: boolean };
  };
  planetaryPositions: { [planet: string]: number };
  auspiciousActivities: string[];
  avoidActivities: string[];
  recommendations: string[];
  specialConsiderations?: string[];
  alternativeTimes?: {
    date: string;
    time: string;
    quality: string;
  }[];
}

/**
 * 💡 CHART INSIGHT - Individual chart interpretation
 */
export interface ChartInsight {
  id: string;
  type: 'strength' | 'challenge' | 'opportunity' | 'karma' | 'talent' | 'warning';
  category: 'career' | 'relationships' | 'health' | 'wealth' | 'spiritual' | 'general';
  priority: 'high' | 'medium' | 'low';
  confidence: number;
  title: string;
  description: string;
  details: string;
  planetaryFactors: string[];
  houseInfluences: number[];
  timing?: {
    activeFrom?: string;
    activeTo?: string;
    peakPeriod?: string;
  };
  recommendations: string[];
  remedies?: string[];
  relatedInsights?: string[];
}

/**
 * 📋 CHART SUMMARY - Quick overview
 */
export interface ChartSummary {
  sunSign: string;
  moonSign: string;
  risingSign: string;
  dominantPlanet: string;
  dominantElement: string;
  yogaCount: number;
  currentDasha: string;
  overallStrength: number;
  lifeTheme: string;
  keyStrengths: string[];
  keyChallenges: string[];
  recommendations: string[];
}

/**
 * 👤 BIRTH PROFILE DATA - User profile information
 */
export interface BirthProfileData {
  firstName: string;
  lastName?: string;
  birthDateTime: Date | string;
  birthLocation: string;
  birthLatitude: number;
  birthLongitude: number;
  timezone: string;
  gender?: 'male' | 'female' | 'other';
  notes?: string;
}

// ================ ADDITIONAL SUPPORTING TYPES ================

/**
 * Chart Calculation Parameters
 */
export interface ChartCalculationParams {
  birthData: BirthData;
  chartType: 'natal' | 'divisional' | 'transit' | 'muhurta';
  ayanamsaType: 'lahiri' | 'raman' | 'krishnamurti';
  houseSystem: 'placidus' | 'whole-sign' | 'equal';
  includeDivisionalCharts?: boolean;
  includeNakshatras?: boolean;
  includeYogaAnalysis?: boolean;
}

/**
 * User Preferences for Analysis
 */
export interface UserAnalysisPreferences {
  preferredLanguage: 'english' | 'hindi' | 'sanskrit';
  analysisDepth: 'basic' | 'intermediate' | 'advanced' | 'professional';
  includeRemedies: boolean;
  includePredictions: boolean;
  focusAreas: LifeArea[];
  notificationPreferences: NotificationPreference[];
}

/**
 * Life Areas for Focus
 */
export type LifeArea = 'career' | 'relationships' | 'health' | 'wealth' | 'education' | 'family' | 'spiritual' | 'travel';

/**
 * Notification Preferences
 */
export interface NotificationPreference {
  type: 'transit_alerts' | 'dasha_changes' | 'auspicious_times' | 'remedy_reminders';
  enabled: boolean;
  frequency: 'daily' | 'weekly' | 'monthly' | 'major_events_only';
}

/**
 * API Response Wrapper
 */
export interface ApiResponse<T> {
  success: boolean;
  data?: T;
  error?: string;
  message?: string;
  timestamp: string;
  requestId?: string;
}

/**
 * Error Types for Better Error Handling
 */
export interface AstrologyServiceError {
  code: string;
  message: string;
  details?: any;
  suggestions?: string[];
}

/**
 * Extended User Preferences
 */
export interface ExtendedUserPreferences extends UserAnalysisPreferences {
  chartStyle: 'traditional' | 'modern' | 'simplified';
  colorScheme: 'classic' | 'vibrant' | 'monochrome';
  defaultChartType: string;
  showDegrees: boolean;
  showNakshatras: boolean;
  showAspects: boolean;
  aspectOrbs: { [aspect: string]: number };
}

/**
 * Service Status Information
 */
export interface ServiceStatus {
  isHealthy: boolean;
  hasAdvancedFeatures: boolean;
  version: string;
  lastUpdated: string;
  endpoints: {
    [endpoint: string]: {
      available: boolean;
      responseTime?: number;
      lastChecked: string;
    };
  };
}

/**
 * Analysis Request Base
 */
export interface AnalysisRequestBase {
  requestId?: string;
  timestamp?: string;
  preferences?: ExtendedUserPreferences;
  includeRemedies?: boolean;
  includePredictions?: boolean;
  detailLevel?: 'basic' | 'standard' | 'comprehensive';
}

/**
 * Batch Analysis Request
 */
export interface BatchAnalysisRequest extends AnalysisRequestBase {
  charts: string[];
  analysisTypes: ('yoga' | 'dasha' | 'compatibility' | 'transit' | 'remedial')[];
  priorityOrder?: string[];
}

/**
 * Export Options
 */
export interface ExportOptions {
  format: 'json' | 'pdf' | 'csv' | 'xml';
  includeCharts: boolean;
  includeInsights: boolean;
  includeRemedies: boolean;
  templateStyle?: string;
  customFields?: { [key: string]: any };
}

// ================ ENUMS ================

/**
 * Chart Types
 */
export enum ChartType {
  NATAL = 'natal',
  NAVAMSA = 'navamsa', 
  DASHAMSA = 'dashamsa',
  TRANSIT = 'transit',
  SOLAR_RETURN = 'solar_return',
  LUNAR_RETURN = 'lunar_return'
}

/**
 * Planet Names
 */
export enum Planet {
  SUN = 'Sun',
  MOON = 'Moon',
  MERCURY = 'Mercury',
  VENUS = 'Venus',
  MARS = 'Mars',
  JUPITER = 'Jupiter',
  SATURN = 'Saturn',
  RAHU = 'Rahu',
  KETU = 'Ketu'
}

/**
 * Zodiac Signs
 */
export enum ZodiacSign {
  ARIES = 'Aries',
  TAURUS = 'Taurus',
  GEMINI = 'Gemini',
  CANCER = 'Cancer',
  LEO = 'Leo',
  VIRGO = 'Virgo',
  LIBRA = 'Libra',
  SCORPIO = 'Scorpio',
  SAGITTARIUS = 'Sagittarius',
  CAPRICORN = 'Capricorn',
  AQUARIUS = 'Aquarius',
  PISCES = 'Pisces'
}

/**
 * House Numbers
 */
export enum House {
  FIRST = 1,
  SECOND = 2,
  THIRD = 3,
  FOURTH = 4,
  FIFTH = 5,
  SIXTH = 6,
  SEVENTH = 7,
  EIGHTH = 8,
  NINTH = 9,
  TENTH = 10,
  ELEVENTH = 11,
  TWELFTH = 12
}

// ================ TYPE GUARDS ================

/**
 * Type guard to check if response is a complete analysis
 */
export function isCompleteAnalysisResponse(obj: any): obj is CompleteAnalysisResponse {
  return obj && typeof obj === 'object' &&
         'personalizedMessage' in obj &&
         'birthChart' in obj &&
         'yogaAnalysis' in obj &&
         'dashaAnalysis' in obj &&
         'remedialRecommendations' in obj;
}

/**
 * Type guard for API responses
 */
export function isApiResponse<T>(obj: any): obj is ApiResponse<T> {
  return obj && typeof obj === 'object' &&
         typeof obj.success === 'boolean' &&
         'timestamp' in obj;
}

/**
 * Type guard for BirthChart
 */
export function isBirthChart(obj: any): obj is BirthChart {
  return obj && typeof obj === 'object' &&
         typeof obj.id === 'string' &&
         typeof obj.chartType === 'string' &&
         'birthData' in obj &&
         'chartData' in obj;
}

/**
 * Type guard for CompatibilityResult
 */
export function isCompatibilityResult(obj: any): obj is CompatibilityResult {
  return obj && typeof obj === 'object' &&
         'person1' in obj &&
         'person2' in obj &&
         'overallScore' in obj &&
         'compatibility' in obj;
}

/**
 * Type guard for DashaData
 */
export function isDashaData(obj: any): obj is DashaData {
  return obj && typeof obj === 'object' &&
         'system' in obj &&
         'currentPeriods' in obj &&
         'upcomingTransitions' in obj;
}

// ================ UTILITY TYPES ================

/**
 * Partial types for updates
 */
export type PartialBirthData = Partial<BirthData>;
export type PartialUserPreferences = Partial<UserAnalysisPreferences>;

/**
 * Required fields for chart calculation
 */
export type RequiredBirthData = Required<Pick<BirthData, 'birthDateTime' | 'birthLatitude' | 'birthLongitude'>>;

// ================ TYPE UNIONS AND LITERAL TYPES ================

export type PlanetName = 'Sun' | 'Moon' | 'Mercury' | 'Venus' | 'Mars' | 'Jupiter' | 'Saturn' | 'Rahu' | 'Ketu';

export type SignName = 'Aries' | 'Taurus' | 'Gemini' | 'Cancer' | 'Leo' | 'Virgo' | 
                      'Libra' | 'Scorpio' | 'Sagittarius' | 'Capricorn' | 'Aquarius' | 'Pisces';

export type HouseNumber = 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 | 9 | 10 | 11 | 12;

export type AnalysisType = 'yoga' | 'dasha' | 'compatibility' | 'transit' | 'remedial' | 'muhurta' | 'prashna';

export type ChartSystemType = 'Vedic' | 'Western' | 'Chinese' | 'Mayan';

export type TimeUnitType = 'seconds' | 'minutes' | 'hours' | 'days' | 'months' | 'years';

// ================ CONSTANTS AND ENUMS ================

export const PLANET_COLORS: { [key in PlanetName]: string } = {
  'Sun': '#FF6B35',
  'Moon': '#4A90E2',
  'Mercury': '#50C878',
  'Venus': '#FF69B4',
  'Mars': '#DC143C',
  'Jupiter': '#FFD700',
  'Saturn': '#4B0082',
  'Rahu': '#8B4513',
  'Ketu': '#708090'
};

export const SIGN_ELEMENTS: { [key in SignName]: string } = {
  'Aries': 'Fire', 'Leo': 'Fire', 'Sagittarius': 'Fire',
  'Taurus': 'Earth', 'Virgo': 'Earth', 'Capricorn': 'Earth',
  'Gemini': 'Air', 'Libra': 'Air', 'Aquarius': 'Air',
  'Cancer': 'Water', 'Scorpio': 'Water', 'Pisces': 'Water'
};

export const HOUSE_MEANINGS: { [key in HouseNumber]: string } = {
  1: 'Self, Personality, Physical Body',
  2: 'Wealth, Family, Speech',
  3: 'Siblings, Communication, Short Journeys',
  4: 'Home, Mother, Happiness',
  5: 'Children, Education, Creativity',
  6: 'Health, Enemies, Service',
  7: 'Partnership, Marriage, Business',
  8: 'Longevity, Transformation, Secrets',
  9: 'Higher Learning, Philosophy, Fortune',
  10: 'Career, Status, Father',
  11: 'Gains, Friends, Aspirations',
  12: 'Loss, Spirituality, Foreign Lands'
};
