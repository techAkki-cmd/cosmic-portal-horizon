import api from './api';

export interface PlanetaryPosition {
  planet: string;
  sign: string;
  degree: number;
  isRetrograde: boolean;
  house?: number;
}

export interface BirthChart {
  id: string;
  userId: string;
  name: string;
  birthDate: string;
  birthTime: string;
  birthPlace: string;
  coordinates: {
    latitude: number;
    longitude: number;
  };
  planets: PlanetaryPosition[];
  houses: number[];
  aspects: Array<{
    planet1: string;
    planet2: string;
    aspect: string;
    orb: number;
  }>;
  interpretation?: string;
  createdAt: string;
}

export interface CosmicWeather {
  date: string;
  planetaryTransits: PlanetaryPosition[];
  moonPhase: string;
  significantAspects: Array<{
    aspect: string;
    planets: string[];
    influence: string;
    intensity: number;
  }>;
  dailyInsight: string;
  lifeAreaInfluences: {
    love: number;
    career: number;
    health: number;
    finances: number;
    spirituality: number;
  };
}

export interface PersonalizedMessage {
  message: string;
  transitInfluence: string;
  recommendation: string;
  intensity: number;
}

class AstrologyService {
  // Birth Chart Services
  async generateBirthChart(data: {
    name: string;
    birthDate: string;
    birthTime: string;
    birthPlace: string;
  }): Promise<BirthChart> {
    const response = await api.post('/astrology/birth-chart', data);
    return response.data;
  }

  async getUserBirthCharts(): Promise<BirthChart[]> {
    const response = await api.get('/astrology/birth-charts');
    return response.data;
  }

  async getBirthChart(id: string): Promise<BirthChart> {
    const response = await api.get(`/astrology/birth-chart/${id}`);
    return response.data;
  }

  // Planetary Data Services
  async getCurrentPlanetaryPositions(): Promise<PlanetaryPosition[]> {
    const response = await api.get('/astrology/planetary-positions');
    return response.data;
  }

  async getCosmicWeather(date?: string): Promise<CosmicWeather> {
    const params = date ? { date } : {};
    const response = await api.get('/astrology/cosmic-weather', { params });
    return response.data;
  }

  // Personalized Services
  async getPersonalizedMessage(): Promise<PersonalizedMessage> {
    const response = await api.get('/astrology/personalized-message');
    return response.data;
  }

  async getDailyHoroscope(): Promise<{
    sign: string;
    horoscope: string;
    luckyNumbers: number[];
    luckyColors: string[];
  }> {
    const response = await api.get('/astrology/daily-horoscope');
    return response.data;
  }

  // Advanced Tools
  async getCompatibilityReport(data: {
    chart1Id: string;
    chart2Id: string;
  }): Promise<{
    compatibility: number;
    strengths: string[];
    challenges: string[];
    advice: string;
  }> {
    const response = await api.post('/astrology/compatibility', data);
    return response.data;
  }

  async getTransitForecast(data: {
    chartId: string;
    period: 'week' | 'month' | 'year';
  }): Promise<{
    period: string;
    majorTransits: Array<{
      planet: string;
      aspect: string;
      date: string;
      influence: string;
    }>;
    advice: string;
  }> {
    const response = await api.post('/astrology/transit-forecast', data);
    return response.data;
  }
}

export const astrologyService = new AstrologyService();
export default astrologyService;