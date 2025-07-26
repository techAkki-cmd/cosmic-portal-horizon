// src/services/astrologyService.ts
import { authService } from './authService';

export interface PersonalizedMessage {
  message: string;
  transitInfluence: string;
  recommendation: string;
  intensity: number;
  dominantPlanet?: string;
  luckyColor?: string;
  bestTimeOfDay?: string;
  moonPhase?: string;
}

export interface LifeAreaInfluence {
  title: string;
  rating: number;
  insight: string;
  icon: string;
  gradient: string;
}

export interface UserStats {
  chartsCreated: number;
  accuracyRate: number;
  cosmicEnergy: string;
  streakDays: number;
}

class AstrologyService {
  private baseURL = import.meta.env.VITE_API_URL || 'http://localhost:8080/api';

  async getPersonalizedMessage(): Promise<PersonalizedMessage> {
    try {
      const response = await authService.authenticatedRequest('/astrology/personalized-message');
      
      if (!response.ok) {
        throw new Error('Failed to fetch personalized message');
      }
      
      return await response.json();
    } catch (error) {
      console.error('❌ Personalized message fetch error:', error);
      throw error;
    }
  }

  async getLifeAreaInfluences(): Promise<LifeAreaInfluence[]> {
    try {
      const response = await authService.authenticatedRequest('/astrology/life-area-influences');
      
      if (!response.ok) {
        throw new Error('Failed to fetch life area influences');
      }
      
      return await response.json();
    } catch (error) {
      console.error('❌ Life area influences fetch error:', error);
      throw error;
    }
  }

  async getUserStats(): Promise<UserStats> {
    try {
      const response = await authService.authenticatedRequest('/astrology/user-stats');
      
      if (!response.ok) {
        throw new Error('Failed to fetch user stats');
      }
      
      return await response.json();
    } catch (error) {
      console.error('❌ User stats fetch error:', error);
      throw error;
    }
  }

  async getRecentCharts(): Promise<{ id: string; name: string; date: string }[]> {
    try {
      const response = await authService.authenticatedRequest('/astrology/recent-charts');
      
      if (!response.ok) {
        // Return empty array if endpoint doesn't exist yet
        return [];
      }
      
      return await response.json();
    } catch (error) {
      console.error('❌ Recent charts fetch error:', error);
      return [];
    }
  }
}

export const astrologyService = new AstrologyService();
