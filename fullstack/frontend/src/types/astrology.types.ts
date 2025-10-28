// src/types/astrology.types.ts
export interface BirthData {
  birthDateTime: string;
  birthLocation: string;
  birthLatitude: number;
  birthLongitude: number;
  timezone: string;
}

export interface PlanetPosition {
  planet: string;
  degree: number;
  sign: string;
  nakshatra?: string;
  pada?: number;
  strength: number;
  isRetrograde?: boolean;
}

export interface VedicYoga {
  name: string;
  type: 'raja' | 'dhana' | 'spiritual' | 'challenging';
  description: string;
  rarity: number;
  strength: 'weak' | 'moderate' | 'strong' | 'excellent';
  remedy: string;
}

export interface DashaPeriod {
  mahadashaLord: string;
  antardashaLord?: string;
  startDate: string;
  endDate: string;
  isCurrent: boolean;
  theme: string;
  opportunities: string;
  challenges: string;
  remedies: string;
}

export interface Remedy {
  category: 'gemstone' | 'mantra' | 'charity' | 'lifestyle';
  title: string;
  description: string;
  instructions: string;
  timing: string;
  priority: 1 | 2 | 3 | 4 | 5;
}

export interface BirthChart {
  // Core Data
  planets: PlanetPosition[];
  ascendant: string;
  sunSign: string;
  moonSign: string;
  
  // Analysis
  yogas: VedicYoga[];
  dashas: DashaPeriod[];
  remedies: Remedy[];
  
  // Element Analysis
  elementDistribution: {
    fire: number;
    earth: number;
    air: number;
    water: number;
  };
  dominantElement: string;
  
  // Metadata
  uniquenessHighlight: string;
  calculatedAt: string;
}
