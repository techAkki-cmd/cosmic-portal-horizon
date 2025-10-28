// src/services/astrologyService.ts

import { authService } from './authService';
import type {
  PersonalizedMessage,
  LifeAreaInfluence,
  AstrologyUserStats,
  YogaAnalysisResponse,
  DashaAnalysisResponse,
  RemedialRecommendationsResponse,
  TransitResponse,
  BirthChartResponse,
  CompleteAnalysisResponse,
  BirthChart,
  TransitForecast,
  DashaData,
  CompatibilityResult,
  MuhurtaResult,
  ChartInsight
} from '../types/astrology';
import type { BirthProfileData } from '../types/auth';

// ================ SERVICE INTERFACES ================

interface ChartGenerationRequest {
  birthData: BirthProfileData;
  chartType: 'Natal' | 'Transit' | 'Prashna' | 'Muhurta';
  preferences?: {
    ayanamsa: 'Lahiri' | 'Raman' | 'KP' | 'Tropical';
    houseSystem: 'Placidus' | 'Equal' | 'Whole';
    showRetrogrades: boolean;
    includeMinorAspects: boolean;
  };
}

interface CompatibilityRequest {
  person1: BirthProfileData;
  person2: BirthProfileData;
  analysisType: 'Basic' | 'Detailed' | 'Kulakoota';
}

interface MuhurtaRequest {
  purpose: string;
  dateRange: {
    startDate: string;
    endDate: string;
  };
  location: {
    latitude: number;
    longitude: number;
    timezone: string;
  };
  preferences?: {
    timeOfDay: 'Morning' | 'Afternoon' | 'Evening' | 'Any';
    avoidDays: string[];
  };
}

interface ApiError {
  message: string;
  code?: string;
  details?: any;
}

// ================ MAIN SERVICE CLASS ================

export class AstrologyService {
  private readonly baseURL: string = import.meta.env.VITE_API_URL || 'http://localhost:8080/api/birth-chart';
  private readonly cacheKey: string = 'cosmic_astrology_cache';
  private readonly cacheExpiry: number = 5 * 60 * 1000; // 5 minutes

  // ================ CORE HTTP METHODS ================

  // ✅ Enhanced makeRequest method in astrologyService.ts
private async makeRequest<T>(endpoint: string, options?: RequestInit): Promise<T> {
  try {
    const token = authService.getToken();
    const url = `${this.baseURL}${endpoint}`;
    
    const response = await fetch(url, {
      headers: {
        'Content-Type': 'application/json',
        ...(token ? { 'Authorization': `Bearer ${token}` } : {}),
        ...options?.headers,
      },
      ...options,
    });

    if (!response.ok) {
      const errorText = await response.text();
      console.error(`❌ API Error (${response.status}):`, errorText);
      
      if (response.status === 401) {
        throw new Error('Authentication required');
      } else if (response.status === 405) {
        throw new Error('Method not supported');
      } else {
        throw new Error(`HTTP ${response.status}: ${response.statusText}`);
      }
    }

    return await response.json();
  } catch (error) {
    console.error(`❌ API Request failed for ${endpoint}:`, error);
    throw error;
  }
}


  private async handleErrorResponse(response: Response): Promise<ApiError> {
    try {
      const contentType = response.headers.get('content-type');
      if (contentType?.includes('application/json')) {
        return await response.json();
      } else {
        const text = await response.text();
        return { message: text || 'An error occurred' };
      }
    } catch {
      return { message: 'Failed to parse error response' };
    }
  }

  private getCurrentUsername(): string {
    const username = authService.getCurrentUser()?.username || localStorage.getItem('username');
    if (!username) {
      throw new Error('User not authenticated. Please log in first.');
    }
    return username;
  }

  // ================ COMPREHENSIVE ANALYSIS METHODS ================

  /**
   * 🔥 GET COMPLETE ANALYSIS - Combines all advanced features
   */
  async getCompleteAnalysis(): Promise<CompleteAnalysisResponse> {
    try {
      const username = this.getCurrentUsername();
      
      // First try the comprehensive endpoint if it exists
      try {
        return await this.makeRequest<CompleteAnalysisResponse>(`/complete-analysis?username=${encodeURIComponent(username)}`);
      } catch (error) {
        console.log('📝 Comprehensive endpoint not available, combining individual calls...');
        
        // Fallback: Combine multiple API calls
        const [
          personalizedMessage,
          birthChart,
          yogaAnalysis,
          dashaAnalysis,
          remedialRecommendations,
          currentTransits,
          lifeAreaInfluences,
          userStats
        ] = await Promise.allSettled([
          this.getPersonalizedMessage(),
          this.getUserBirthChart().catch(() => null),
          this.getYogaAnalysis().catch(() => null),
          this.getDashaAnalysis().catch(() => null),
          this.getRemedialRecommendations().catch(() => null),
          this.getCurrentTransits(),
          this.getLifeAreaInfluences(),
          this.getUserStats()
        ]);

        return {
          personalizedMessage: personalizedMessage.status === 'fulfilled' ? personalizedMessage.value : null,
          birthChart: birthChart.status === 'fulfilled' ? birthChart.value : null,
          yogaAnalysis: yogaAnalysis.status === 'fulfilled' ? yogaAnalysis.value : null,
          dashaAnalysis: dashaAnalysis.status === 'fulfilled' ? dashaAnalysis.value : null,
          remedialRecommendations: remedialRecommendations.status === 'fulfilled' ? remedialRecommendations.value : null,
          currentTransits: currentTransits.status === 'fulfilled' ? currentTransits.value : [],
          lifeAreaInfluences: lifeAreaInfluences.status === 'fulfilled' ? lifeAreaInfluences.value : [],
          userStats: userStats.status === 'fulfilled' ? userStats.value : null,
          analysisTimestamp: new Date().toISOString(),
          analysisId: `analysis_${Date.now()}`
        };
      }
    } catch (error) {
      console.error('❌ Complete analysis error:', error);
      throw error;
    }
  }

  
  
  async getCurrentTransits(birthData?: any): Promise<TransitResponse[]> {
  try {
    if (birthData) {
      console.log('🔄 POST: Using provided birth data for transits');
      
      // ✅ CRITICAL: Include authorization header for POST requests
      const token = authService.getToken();
      const headers: Record<string, string> = {
        'Content-Type': 'application/json',
      };
      
      // Only add auth header if token exists to avoid 401s
      if (token) {
        headers['Authorization'] = `Bearer ${token}`;
      }
      
      try {
        return await this.makeRequest<TransitResponse[]>('/current-transits', {
          method: 'POST',
          headers,
          body: JSON.stringify(birthData)
        });
      } catch (postError) {
        console.warn('📡 POST method failed, trying GET fallback');
        // Fall through to GET method below
      }
    }
    
    // ✅ GET method fallback (works on dashboard)
    console.log('🔄 GET: Using username-based transits');
    const username = this.getCurrentUsername();
    return await this.makeRequest<TransitResponse[]>(`/current-transits?username=${encodeURIComponent(username)}`);
    
  } catch (error) {
    console.error('❌ Current transits error:', error);
    
    // ✅ Enhanced fallback with location info
    return this.createLocationAwareFallback(birthData);
  }
}
private createLocationAwareFallback(birthData?: any): TransitResponse[] {
  const location = birthData?.birthLocation || "your location";
  const now = new Date();
  
  return [
    {
      planet: "Sun",
      position: 120.5 + (now.getTime() % 30),
      sign: "Leo",
      nakshatra: "Magha",
      pada: 2,
      influence: `Solar energy enhances leadership and vitality for ${location}`,
      isRetrograde: false,
      calculatedAt: now.toISOString()
    },
    {
      planet: "Jupiter", 
      position: 67.8 + (now.getDate() % 30),
      sign: "Gemini",
      nakshatra: "Punarvasu", 
      pada: 3,
      influence: `Jupiter brings expansion and wisdom opportunities in ${location}`,
      isRetrograde: false,
      calculatedAt: now.toISOString()
    },
    {
      planet: "Saturn",
      position: 280.5 + (now.getMonth() * 2),
      sign: "Capricorn",
      nakshatra: "Uttara Ashadha",
      pada: 1,
      influence: `Saturn emphasizes discipline and long-term planning for ${location}`, 
      isRetrograde: false,
      calculatedAt: now.toISOString()
    }
  ] as TransitResponse[];
}

  /**
   * 🔥 GET YOGA ANALYSIS - Comprehensive yoga detection
   */
  async getYogaAnalysis(): Promise<YogaAnalysisResponse> {
    try {
      const username = this.getCurrentUsername();
      return await this.makeRequest<YogaAnalysisResponse>(`/yoga-analysis?username=${encodeURIComponent(username)}`);
    } catch (error) {
      console.error('❌ Yoga analysis error:', error);
      throw error;
    }
  }

  /**
   * 🔥 GET DASHA ANALYSIS - Vimshottari dasha periods
   */
  async getDashaAnalysis(): Promise<DashaAnalysisResponse> {
    try {
      const username = this.getCurrentUsername();
      return await this.makeRequest<DashaAnalysisResponse>(`/dasha-analysis?username=${encodeURIComponent(username)}`);
    } catch (error) {
      console.error('❌ Dasha analysis error:', error);
      throw error;
    }
  }

  /**
   * 🔥 GET REMEDIAL RECOMMENDATIONS - Personalized remedies
   */
  async getRemedialRecommendations(): Promise<RemedialRecommendationsResponse> {
    try {
      const username = this.getCurrentUsername();
      return await this.makeRequest<RemedialRecommendationsResponse>(`/remedial-recommendations?username=${encodeURIComponent(username)}`);
    } catch (error) {
      console.error('❌ Remedial recommendations error:', error);
      throw error;
    }
  }

  // ================ DASHBOARD DATA METHODS ================

  async getPersonalizedMessage(): Promise<PersonalizedMessage> {
    try {
      const cached = this.getCachedData('personalizedMessage');
      if (cached) return cached;

      const username = this.getCurrentUsername();
      const data = await this.makeRequest<PersonalizedMessage>(`/personalized-message?username=${encodeURIComponent(username)}`);
      
      this.setCachedData('personalizedMessage', data);
      return data;
    } catch (error) {
      console.error('❌ Personalized message error:', error);
      throw error;
    }
  }

  async getLifeAreaInfluences(): Promise<LifeAreaInfluence[]> {
    try {
      const cached = this.getCachedData('lifeAreaInfluences');
      if (cached) return cached;

      const username = this.getCurrentUsername();
      const data = await this.makeRequest<LifeAreaInfluence[]>(`/life-area-influences?username=${encodeURIComponent(username)}`);
      
      this.setCachedData('lifeAreaInfluences', data);
      return data;
    } catch (error) {
      console.error('❌ Life area influences error:', error);
      throw error;
    }
  }

  async getUserStats(): Promise<AstrologyUserStats> {
    try {
      const cached = this.getCachedData('userStats');
      if (cached) return cached;

      const username = this.getCurrentUsername();
      const data = await this.makeRequest<AstrologyUserStats>(`/user-stats?username=${encodeURIComponent(username)}`);
      
      this.setCachedData('userStats', data);
      return data;
    } catch (error) {
      console.error('❌ User stats error:', error);
      // Return fallback stats
      return {
        chartsCreated: 0,
        accuracyRate: 95,
        cosmicEnergy: 'Harmonious',
        streakDays: 0,
        totalReadings: 0,
        favoriteChartType: 'Natal',
        mostActiveTimeOfDay: 'Morning',
        averageSessionDuration: 0,
        totalPredictions: 0,
        correctPredictions: 0
      };
    }
  }

  async getUserBirthChart(): Promise<BirthChartResponse> {
    try {
      const username = this.getCurrentUsername();
      return await this.makeRequest<BirthChartResponse>(`/current?username=${encodeURIComponent(username)}`);
    } catch (error) {
      console.error('❌ Birth chart error:', error);
      throw error;
    }
  }

  async getRecentCharts(): Promise<BirthChart[]> {
    try {
      return await this.makeRequest<BirthChart[]>('/recent-charts');
    } catch (error) {
      console.error('❌ Recent charts error:', error);
      return [];
    }
  }

  // ================ CHART GENERATION METHODS ================

  async generateBirthChart(request: ChartGenerationRequest): Promise<BirthChart> {
    try {
      const data = await this.makeRequest<BirthChart>('/generate-chart', {
        method: 'POST',
        body: JSON.stringify(request)
      });
      
      // Clear relevant caches
      this.clearCachedData(['userStats', 'recentCharts']);
      
      return data;
    } catch (error) {
      console.error('❌ Chart generation error:', error);
      throw error;
    }
  }

  async calculateBirthChart(birthData: BirthProfileData): Promise<BirthChartResponse> {
    try {
      return await this.makeRequest<BirthChartResponse>('/calculate', {
        method: 'POST',
        body: JSON.stringify(birthData)
      });
    } catch (error) {
      console.error('❌ Birth chart calculation error:', error);
      throw error;
    }
  }

  async getChartById(chartId: string): Promise<BirthChart> {
    try {
      return await this.makeRequest<BirthChart>(`/charts/${chartId}`);
    } catch (error) {
      console.error('❌ Chart fetch error:', error);
      throw error;
    }
  }

  async updateChart(chartId: string, updates: Partial<BirthChart>): Promise<BirthChart> {
    try {
      return await this.makeRequest<BirthChart>(`/charts/${chartId}`, {
        method: 'PUT',
        body: JSON.stringify(updates)
      });
    } catch (error) {
      console.error('❌ Chart update error:', error);
      throw error;
    }
  }

  async deleteChart(chartId: string): Promise<void> {
    try {
      await this.makeRequest<void>(`/charts/${chartId}`, {
        method: 'DELETE'
      });
      
      // Clear relevant caches
      this.clearCachedData(['userStats', 'recentCharts']);
    } catch (error) {
      console.error('❌ Chart deletion error:', error);
      throw error;
    }
  }

  // ================ ADVANCED ASTROLOGY METHODS ================

  async getTransitForecast(days: number = 30): Promise<TransitForecast[]> {
    try {
      return await this.makeRequest<TransitForecast[]>(`/transits?days=${days}`);
    } catch (error) {
      console.error('❌ Transit forecast error:', error);
      throw error;
    }
  }

  async getDashaData(): Promise<DashaData> {
    try {
      const cached = this.getCachedData('dashaData');
      if (cached) return cached;

      const data = await this.makeRequest<DashaData>('/dasha');
      
      // Cache for longer as Dasha changes infrequently
      this.setCachedData('dashaData', data, 24 * 60 * 60 * 1000); // 24 hours
      
      return data;
    } catch (error) {
      console.error('❌ Dasha data error:', error);
      throw error;
    }
  }

  async getCompatibilityAnalysis(request: CompatibilityRequest): Promise<CompatibilityResult> {
    try {
      const validationErrors = this.validateCompatibilityRequest(request);
      if (validationErrors.length > 0) {
        throw new Error(`Validation errors: ${validationErrors.join(', ')}`);
      }

      return await this.makeRequest<CompatibilityResult>('/compatibility', {
        method: 'POST',
        body: JSON.stringify(request)
      });
    } catch (error) {
      console.error('❌ Compatibility analysis error:', error);
      throw error;
    }
  }

  async getMuhurtaRecommendations(request: MuhurtaRequest): Promise<MuhurtaResult[]> {
    try {
      return await this.makeRequest<MuhurtaResult[]>('/muhurta', {
        method: 'POST',
        body: JSON.stringify(request)
      });
    } catch (error) {
      console.error('❌ Muhurta recommendations error:', error);
      throw error;
    }
  }

  async getChartInsights(chartId: string): Promise<ChartInsight[]> {
    try {
      return await this.makeRequest<ChartInsight[]>(`/charts/${chartId}/insights`);
    } catch (error) {
      console.error('❌ Chart insights error:', error);
      throw error;
    }
  }

  // ================ CACHING METHODS ================

  private getCachedData(key: string): any {
    try {
      const cache = localStorage.getItem(this.cacheKey);
      if (!cache) return null;

      const parsedCache = JSON.parse(cache);
      const item = parsedCache[key];

      if (!item) return null;

      const now = Date.now();
      if (now > item.expiry) {
        this.clearCachedData([key]);
        return null;
      }

      return item.data;
    } catch (error) {
      console.error('Cache read error:', error);
      return null;
    }
  }

  private setCachedData(key: string, data: any, customExpiry?: number): void {
    try {
      const cache = localStorage.getItem(this.cacheKey);
      const parsedCache = cache ? JSON.parse(cache) : {};

      const expiry = Date.now() + (customExpiry || this.cacheExpiry);

      parsedCache[key] = {
        data,
        expiry
      };

      localStorage.setItem(this.cacheKey, JSON.stringify(parsedCache));
    } catch (error) {
      console.error('Cache write error:', error);
    }
  }

  private clearCachedData(keys?: string[]): void {
    try {
      if (!keys) {
        localStorage.removeItem(this.cacheKey);
        return;
      }

      const cache = localStorage.getItem(this.cacheKey);
      if (!cache) return;

      const parsedCache = JSON.parse(cache);

      keys.forEach(key => {
        delete parsedCache[key];
      });

      localStorage.setItem(this.cacheKey, JSON.stringify(parsedCache));
    } catch (error) {
      console.error('Cache clear error:', error);
    }
  }

  // ================ VALIDATION METHODS ================

  validateBirthData(birthData: BirthProfileData): boolean {
    return !!(
      birthData.birthDateTime &&
      birthData.birthLocation &&
      birthData.birthLatitude !== null &&
      birthData.birthLongitude !== null &&
      birthData.timezone
    );
  }

  validateCompatibilityRequest(request: CompatibilityRequest): string[] {
    const errors: string[] = [];

    if (!this.validateBirthData(request.person1)) {
      errors.push('Person 1 birth data is incomplete');
    }

    if (!this.validateBirthData(request.person2)) {
      errors.push('Person 2 birth data is incomplete');
    }

    if (!['Basic', 'Detailed', 'Kulakoota'].includes(request.analysisType)) {
      errors.push('Invalid analysis type');
    }

    return errors;
  }

  // ================ UTILITY METHODS ================

  clearAllCaches(): void {
    this.clearCachedData();
  }

  async refreshDashboardData(): Promise<void> {
    this.clearCachedData(['personalizedMessage', 'lifeAreaInfluences', 'userStats']);
    
    try {
      await Promise.all([
        this.getPersonalizedMessage(),
        this.getLifeAreaInfluences(),
        this.getUserStats(),
        this.getRecentCharts()
      ]);
    } catch (error) {
      console.error('❌ Dashboard refresh error:', error);
      throw error;
    }
  }

  async exportChartData(chartId: string): Promise<Blob> {
    try {
      const chart = await this.getChartById(chartId);
      const insights = await this.getChartInsights(chartId);

      const exportData = {
        chart,
        insights,
        exportDate: new Date().toISOString(),
        version: '1.0'
      };

      return new Blob([JSON.stringify(exportData, null, 2)], {
        type: 'application/json'
      });
    } catch (error) {
      console.error('Chart export error:', error);
      throw new Error('Failed to export chart data');
    }
  }

  // ================ STATUS METHODS ================

  async checkServiceHealth(): Promise<boolean> {
    try {
      await this.makeRequest<any>('/health');
      return true;
    } catch {
      return false;
    }
  }

  async checkAdvancedFeatures(): Promise<boolean> {
    try {
      const username = this.getCurrentUsername();
      await this.makeRequest<any>(`/yoga-analysis?username=${encodeURIComponent(username)}`);
      return true;
    } catch {
      return false;
    }
  }
}

// ================ SINGLETON EXPORT ================

export const astrologyService = new AstrologyService();
export default astrologyService;

// Export types for convenience
export type {
  ChartGenerationRequest,
  CompatibilityRequest,
  MuhurtaRequest
};
