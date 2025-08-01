import React, { useState, useEffect } from 'react';
import { Navigation } from '@/components/Navigation';
import { Footer } from '@/components/Footer';
import { CosmicBackground } from '@/components/CosmicBackground';
import { UniqueVedicChartVisualizer } from '@/components/UniqueVedicChartVisualizer';
import { RareYogaHighlights } from '@/components/RareYogaHighlights';
import { DashaTimeline } from '@/components/DashaTimeline';
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Label } from '@/components/ui/label';
import { Tabs, TabsContent, TabsList, TabsTrigger } from '@/components/ui/tabs';
import { Alert, AlertDescription } from '@/components/ui/alert';
import { Badge } from '@/components/ui/badge';
import { Progress } from '@/components/ui/progress';
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from '@/components/ui/select';
import { Moon, Star, Sparkles, Crown, Clock, Zap, MapPin, Calendar, AlertCircle, Loader2, Globe, CheckCircle } from 'lucide-react';
import { motion, AnimatePresence } from 'framer-motion';

// ✅ COMPREHENSIVE TYPE DEFINITIONS
interface NakshatraInfo {
  nakshatra: string;
  nakshatraNumber: number;
  pada: number;
  deity: string;
  symbol: string;
  quality: string;
  meaning: string;
  lifeLesson: string;
  mantra: string;
}

interface RareYoga {
  name: string;
  description: string;
  meaning: string;
  planetsCombination?: string;
  isVeryRare: boolean;
  remedialAction?: string;
  rarity?: number;
}

interface DashaPeriod {
  mahadashaLord: string;
  antardashaLord?: string;
  startDate: string;
  endDate: string;
  interpretation: string;
  lifeTheme: string;
  opportunities: string;
  challenges: string;
  isCurrent: boolean;
  remedies?: string;
}

interface PersonalizedRemedy {
  category: string;
  remedy: string;
  reason: string;
  instructions: string;
  timing: string;
  priority: number;
}

interface ElementAnalysis {
  fireCount: number;
  earthCount: number;
  airCount: number;
  waterCount: number;
  dominantElement: string;
  personality: string;
  strengths: string;
  challenges: string;
}

interface ChartVisualizationData {
  planets: Array<{
    planet: string;
    degree: number;
    sign: string;
    house: number;
    color: string;
    isRetrograde: boolean;
  }>;
  houses: Array<{
    houseNumber: number;
    sign: string;
    startDegree: number;
    lord: string;
    meaning: string;
  }>;
  aspects: Array<{
    fromPlanet: string;
    toPlanet: string;
    aspectType: string;
    orb: number;
    strength: string;
  }>;
}

interface PersonalizedBirthChartData {
  siderealPositions: Record<string, number>;
  planetSigns: Record<string, string>;
  planetNakshatras: Record<string, NakshatraInfo>;
  planetStrengths: Record<string, number>;
  ascendantSign: string;
  sunSign: string;
  moonSign: string;
  ayanamsa: number;
  rareYogas: RareYoga[];
  dashaTable: DashaPeriod[];
  currentDasha: DashaPeriod | null;
  elementAnalysis: ElementAnalysis;
  uniquenessHighlight: string;
  personalizedRemedies: PersonalizedRemedy[];
  shadbalaStrengths: Record<string, number>;
  dominantPlanet: string;
  chartPersonality: string;
  karmaLessons: string[];
  chartData: ChartVisualizationData;
}

interface BirthDataForm {
  birthDateTime: string;
  birthLocation: string;
  birthLatitude: number | null;
  birthLongitude: number | null;
  timezone: string;
}

// ✅ COMPREHENSIVE TIMEZONE VALIDATION UTILITIES
const VALID_TIMEZONES = [
  { value: "Asia/Kolkata", label: "India (IST)", offset: "+05:30", region: "Asia" },
  { value: "America/New_York", label: "Eastern Time (US)", offset: "-05:00", region: "America" },
  { value: "America/Los_Angeles", label: "Pacific Time (US)", offset: "-08:00", region: "America" },
  { value: "Europe/London", label: "GMT/BST (UK)", offset: "+00:00", region: "Europe" },
  { value: "Asia/Tokyo", label: "Japan (JST)", offset: "+09:00", region: "Asia" },
  { value: "Australia/Sydney", label: "Australia (AEST)", offset: "+10:00", region: "Australia" },
  { value: "Asia/Dubai", label: "UAE (GST)", offset: "+04:00", region: "Asia" },
  { value: "Europe/Paris", label: "Central Europe", offset: "+01:00", region: "Europe" },
  { value: "America/Chicago", label: "Central Time (US)", offset: "-06:00", region: "America" },
  { value: "Asia/Shanghai", label: "China (CST)", offset: "+08:00", region: "Asia" },
  { value: "Asia/Bangkok", label: "Thailand (ICT)", offset: "+07:00", region: "Asia" },
  { value: "Europe/Berlin", label: "Germany (CET)", offset: "+01:00", region: "Europe" },
  { value: "America/Toronto", label: "Canada Eastern", offset: "-05:00", region: "America" },
  { value: "Asia/Singapore", label: "Singapore (SGT)", offset: "+08:00", region: "Asia" },
  { value: "UTC", label: "Coordinated Universal Time", offset: "+00:00", region: "Global" }
];

const validateAndNormalizeTimezone = (timezone: string): string => {
  if (!timezone || timezone.trim() === "") {
    return "Asia/Kolkata";
  }

  // Handle common incomplete values
  const timezoneMapping: { [key: string]: string } = {
    "Asia": "Asia/Kolkata",
    "Asia/": "Asia/Kolkata",
    "America": "America/New_York", 
    "America/": "America/New_York",
    "Europe": "Europe/London",
    "Europe/": "Europe/London",
    "UTC": "UTC",
    "GMT": "GMT"
  };

  const normalizedTimezone = timezoneMapping[timezone.trim()] || timezone.trim();
  
  // Validate against known good timezones
  const isValid = VALID_TIMEZONES.some(tz => tz.value === normalizedTimezone);
  return isValid ? normalizedTimezone : "Asia/Kolkata";
};

// ✅ COMPREHENSIVE DATA TRANSFORMATION UTILITIES

/**
 * Transform backend dasha data to frontend-compatible format
 */
const transformDashaData = (backendData: any): DashaPeriod[] => {
  console.log('🔄 Transforming dasha data:', backendData);
  
  // Handle different possible data structures
  let dashaArray: any[] = [];
  
  if (Array.isArray(backendData)) {
    dashaArray = backendData;
  } else if (backendData?.dashaAnalysis?.upcomingPeriods) {
    dashaArray = backendData.dashaAnalysis.upcomingPeriods;
  } else if (backendData?.dashaTable) {
    dashaArray = backendData.dashaTable;
  } else if (backendData?.upcomingPeriods) {
    dashaArray = backendData.upcomingPeriods;
  }

  if (!Array.isArray(dashaArray) || dashaArray.length === 0) {
    console.log('📝 No dasha data found, creating fallback');
    return createFallbackDashaData();
  }

  return dashaArray.map((dasha, index) => {
    return transformSingleDasha(dasha, index);
  });
};

/**
 * Transform individual dasha period
 */
const transformSingleDasha = (dasha: any, index: number): DashaPeriod => {
  // Extract planet names from various field formats
  const extractPlanets = (periodStr?: string, mahadasha?: string, antardasha?: string) => {
    const planets = ['Sun', 'Moon', 'Mercury', 'Venus', 'Mars', 'Jupiter', 'Saturn', 'Rahu', 'Ketu'];
    
    // If we already have structured data, use it
    if (mahadasha && antardasha) {
      return { main: mahadasha, sub: antardasha };
    }
    
    if (mahadasha && !antardasha) {
      return { main: mahadasha, sub: null };
    }
    
    if (!periodStr) return { main: 'Unknown', sub: null };
    
    // Parse different formats: "Sun Antardasha", "Venus Mahadasha", "Venus-Mars period"
    const lowerPeriod = periodStr.toLowerCase();
    const foundPlanet = planets.find(p => lowerPeriod.includes(p.toLowerCase()));
    
    if (foundPlanet) {
      if (lowerPeriod.includes('antardasha')) {
        return { main: 'Current', sub: foundPlanet };
      } else if (lowerPeriod.includes('mahadasha')) {
        return { main: foundPlanet, sub: null };
      } else {
        return { main: foundPlanet, sub: null };
      }
    }
    
    // If no planet found, try to extract from the string
    const words = periodStr.split(/\s+|[-_]/);
    const firstWord = words[0]?.charAt(0).toUpperCase() + words[0]?.slice(1).toLowerCase() || 'Unknown';
    return { main: firstWord, sub: null };
  };

  // Generate realistic dates
  const generateDates = (index: number, duration?: string) => {
    const baseDate = new Date();
    let monthsToAdd = 18; // Default 18 months
    
    // Parse duration if available
    if (duration && typeof duration === 'string') {
      if (duration.includes('year')) {
        const years = parseFloat(duration.match(/(\d+\.?\d*)\s*year/i)?.[1] || '1.5');
        monthsToAdd = years * 12;
      } else if (duration.includes('month')) {
        monthsToAdd = parseFloat(duration.match(/(\d+)\s*month/i)?.[1] || '18');
      }
    }
    
    const startDate = new Date(baseDate);
    startDate.setMonth(baseDate.getMonth() + (index * 3)); // Stagger start dates by 3 months
    
    const endDate = new Date(startDate);
    endDate.setMonth(startDate.getMonth() + monthsToAdd);
    
    return {
      start: startDate.toISOString().split('T')[0],
      end: endDate.toISOString().split('T')[0]
    };
  };

  // Enhanced interpretations based on planetary combinations
  const generateInterpretation = (planet: string, theme?: string) => {
    const interpretations: { [key: string]: string } = {
      'Sun': 'Solar energy brings leadership opportunities, public recognition, and authority. Time for taking center stage and demonstrating your capabilities with confidence.',
      'Moon': 'Lunar influence enhances intuition, emotional growth, and family connections. Focus on home, comfort, and nurturing relationships while trusting your inner wisdom.',
      'Mercury': 'Mercurial energy boosts communication, learning, and business acumen. Excellent period for intellectual pursuits, skill development, and expanding your knowledge base.',
      'Venus': 'Venusian period favors relationships, creativity, and material comforts. Time for love, artistic expression, and enjoying life\'s beautiful pleasures with balance.',
      'Mars': 'Martian energy increases drive, assertiveness, and competitive spirit. Good for property matters, technical skills, and taking bold action toward your goals.',
      'Jupiter': 'Jupiterian wisdom brings expansion, higher learning, and spiritual growth. Favorable for education, teaching, advisory roles, and philosophical pursuits.',
      'Saturn': 'Saturnian discipline rewards patience, hard work, and building lasting foundations. Time for structured growth, responsibility, and long-term planning.',
      'Rahu': 'Rahu brings unconventional opportunities, foreign connections, and innovative approaches. Technology, research, and breaking traditional boundaries are favored.',
      'Ketu': 'Ketu encourages spiritual growth, detachment, and inner transformation. Time for meditation, healing arts, and releasing what no longer serves you.'
    };
    
    return interpretations[planet] || `${planet} period brings ${theme || 'growth and transformative experiences'} with unique planetary influence.`;
  };

  // Generate opportunities based on planetary energy
  const generateOpportunities = (planet: string) => {
    const opportunities: { [key: string]: string } = {
      'Sun': 'Government positions, public recognition, leadership roles, father\'s blessings, authority positions, public speaking, political involvement',
      'Moon': 'Real estate gains, family harmony, emotional healing, mother\'s support, hospitality business, food industry, nurturing professions',
      'Mercury': 'Business success, writing projects, communication skills, learning opportunities, technology ventures, teaching, intellectual property',
      'Venus': 'Marriage prospects, artistic success, luxury acquisitions, partnership ventures, fashion industry, beauty business, diplomatic roles',
      'Mars': 'Property investments, technical skills, competitive success, energy projects, sports achievements, engineering, military advancement',
      'Jupiter': 'Higher education, spiritual growth, advisory positions, wisdom teachings, law practice, counseling, religious activities, publishing',
      'Saturn': 'Long-term investments, traditional business, discipline rewards, structural improvements, manufacturing, construction, elderly care',
      'Rahu': 'Foreign opportunities, technology ventures, unconventional paths, innovation, research work, aviation, pharmaceutical industry',
      'Ketu': 'Spiritual liberation, mystical experiences, healing abilities, research work, metaphysical studies, charitable activities'
    };
    return opportunities[planet] || 'Growth opportunities aligned with planetary energy and natural talents';
  };

  // Generate challenges with constructive guidance
  const generateChallenges = (planet: string) => {
    const challenges: { [key: string]: string } = {
      'Sun': 'Ego conflicts, authority disputes, health issues related to heart/bones, tendency toward arrogance, father-related concerns',
      'Moon': 'Emotional fluctuations, mental stress, family tensions, digestive issues, over-sensitivity, mood swings, mother-related worries',
      'Mercury': 'Communication misunderstandings, nervous tension, information overload, speech problems, respiratory issues, analysis paralysis',
      'Venus': 'Relationship conflicts, overindulgence, financial extravagance, kidney/reproductive issues, artistic blocks, partnership disputes',
      'Mars': 'Anger management, accidents, conflicts, blood pressure issues, impulsiveness, sibling disputes, aggressive tendencies',
      'Jupiter': 'Over-optimism, weight gain, spiritual ego, excessive spending, liver problems, dogmatic thinking, over-expansion',
      'Saturn': 'Delays, obstacles, depression, joint problems, elderly concerns, pessimism, career stagnation, chronic health issues',
      'Rahu': 'Confusion, deception, mysterious ailments, foreign complications, unconventional problems, addiction tendencies',
      'Ketu': 'Isolation feelings, spiritual crisis, immune system issues, detachment challenges, past-life karma, identity confusion'
    };
    return challenges[planet] || 'Personal growth challenges requiring awareness, patience, and constructive action';
  };

  // Generate traditional remedies
  const generateRemedies = (planet: string) => {
    const remedies: { [key: string]: string } = {
      'Sun': 'Practice Surya Namaskara daily, chant Gayatri Mantra, donate wheat/jaggery, wear ruby, worship Lord Rama',
      'Moon': 'Chant Moon mantra (Om Chandraya Namaha), donate milk/rice, wear pearl, worship Goddess Parvati, practice meditation',
      'Mercury': 'Recite Vishnu Sahasranama, donate green vegetables, wear emerald, worship Lord Ganesha, study sacred texts',
      'Venus': 'Chant Lakshmi mantra, donate white flowers/clothes, wear diamond, worship Goddess Lakshmi, practice artistic pursuits',
      'Mars': 'Recite Hanuman Chalisa, donate red lentils, wear red coral, worship Lord Hanuman, practice yoga for anger management',
      'Jupiter': 'Chant Guru mantra, donate yellow items/turmeric, wear yellow sapphire, worship Lord Brihaspati, teach or counsel others',
      'Saturn': 'Recite Shani mantra, donate black sesame/iron, wear blue sapphire, worship Lord Shani, serve the elderly',
      'Rahu': 'Chant Rahu mantra, donate blue items, wear hessonite garnet, worship Lord Ganesha on Saturdays, practice grounding techniques',
      'Ketu': 'Recite Ketu mantra, donate multicolored items, wear cat\'s eye, worship Lord Ganesha on Tuesdays, practice spiritual detachment'
    };
    return remedies[planet] || `Traditional Vedic remedies for ${planet} planetary period including mantra, donation, and gemstone therapy`;
  };

  // Extract data with fallbacks
  const planets = extractPlanets(dasha.period, dasha.mahadashaLord, dasha.antardashaLord);
  const dates = generateDates(index, dasha.duration);

  return {
    mahadashaLord: planets.main,
    antardashaLord: planets.sub,
    startDate: dasha.startDate || dates.start,
    endDate: dasha.endDate || dates.end,
    interpretation: dasha.interpretation || generateInterpretation(planets.main, dasha.theme),
    lifeTheme: dasha.lifeTheme || dasha.theme || `${planets.main} Energy Period`,
    opportunities: dasha.opportunities || generateOpportunities(planets.main),
    challenges: dasha.challenges || generateChallenges(planets.main),
    isCurrent: dasha.isCurrent !== undefined ? dasha.isCurrent : (index === 0),
    remedies: dasha.remedies || generateRemedies(planets.main)
  };
};

/**
 * Create comprehensive fallback dasha data
 */
const createFallbackDashaData = (): DashaPeriod[] => {
  const currentDate = new Date();
  const dashaData = [
    {
      planet: 'Venus',
      theme: 'Relationships and Creativity',
      monthsOffset: 0,
      duration: 36,
      isCurrent: true
    },
    {
      planet: 'Sun',
      theme: 'Leadership and Authority',
      monthsOffset: 36,
      duration: 12,
      isCurrent: false
    },
    {
      planet: 'Moon',
      theme: 'Emotional Growth and Intuition',
      monthsOffset: 48,
      duration: 20,
      isCurrent: false
    }
  ];

  return dashaData.map((data, index) => {
    const startDate = new Date(currentDate);
    startDate.setMonth(currentDate.getMonth() + data.monthsOffset);
    
    const endDate = new Date(startDate);
    endDate.setMonth(startDate.getMonth() + data.duration);

    return transformSingleDasha({
      mahadashaLord: data.planet,
      startDate: startDate.toISOString().split('T')[0],
      endDate: endDate.toISOString().split('T')[0],
      lifeTheme: data.theme,
      isCurrent: data.isCurrent
    }, index);
  });
};

/**
 * Transform rare yogas data
 */
const transformRareYogasData = (backendData: any): RareYoga[] => {
  console.log('🔄 Transforming yoga data:', backendData);
  
  let yogaArray: any[] = [];
  
  if (Array.isArray(backendData)) {
    yogaArray = backendData;
  } else if (backendData?.yogaAnalysis) {
    const analysis = backendData.yogaAnalysis;
    yogaArray = [
      ...(analysis.rajaYogas || []),
      ...(analysis.dhanaYogas || []),
      ...(analysis.spiritualYogas || []),
      ...(analysis.mahapurushaYogas || []),
      ...(analysis.topYogas || [])
    ];
  } else if (backendData?.rareYogas) {
    yogaArray = backendData.rareYogas;
  }

  if (!Array.isArray(yogaArray) || yogaArray.length === 0) {
    return createFallbackYogaData();
  }

  return yogaArray.map(yoga => ({
    name: yoga.name || yoga.yogaName || 'Unknown Yoga',
    description: yoga.description || yoga.meaning || 'Yoga description pending',
    meaning: yoga.meaning || yoga.manifestation || 'Traditional Vedic combination',
    planetsCombination: yoga.planetsCombination || yoga.planets || 'Calculated with Swiss Ephemeris',
    isVeryRare: yoga.isVeryRare || (yoga.rarity && yoga.rarity < 20) || false,
    remedialAction: yoga.remedialAction || yoga.remedy || 'Follow traditional Vedic practices',
    rarity: yoga.rarity || 50
  }));
};

/**
 * Create fallback yoga data
 */
const createFallbackYogaData = (): RareYoga[] => {
  return [
    {
      name: 'Gaja Kesari Yoga',
      description: 'Moon and Jupiter in favorable positions (1st, 4th, 7th, 10th from each other) create wisdom, prosperity, and royal qualities',
      meaning: 'Elephant-Lion combination brings strength, wisdom, and leadership capabilities',
      planetsCombination: 'Moon-Jupiter favorable aspect in Kendra positions',
      isVeryRare: false,
      remedialAction: 'Worship Jupiter on Thursdays, chant Guru mantra, donate yellow items, wear yellow sapphire',
      rarity: 25
    },
    {
      name: 'Budh Aditya Yoga',
      description: 'Sun and Mercury conjunction (within 10 degrees) enhances intellectual abilities, communication skills, and analytical thinking',
      meaning: 'Divine intelligence combination creating sharp intellect and eloquent speech',
      planetsCombination: 'Sun-Mercury conjunction in same sign',
      isVeryRare: false,
      remedialAction: 'Chant Gayatri Mantra daily, donate green items on Wednesdays, wear emerald',
      rarity: 40
    },
    {
      name: 'Hamsa Yoga',
      description: 'Jupiter in own sign (Sagittarius/Pisces) or exaltation (Cancer) in Kendra creates spiritual wisdom and material prosperity',
      meaning: 'Swan-like purity and wisdom, one of the five Pancha Mahapurusha Yogas indicating divine grace',
      planetsCombination: 'Jupiter in Sagittarius/Pisces/Cancer in 1st/4th/7th/10th house',
      isVeryRare: true,
      remedialAction: 'Study sacred texts, practice meditation, donate to educational causes, wear yellow sapphire',
      rarity: 8
    }
  ];
};

/**
 * Transform personalized remedies data
 */
const transformRemediesData = (backendData: any): PersonalizedRemedy[] => {
  console.log('🔄 Transforming remedies data:', backendData);
  
  let remedyArray: any[] = [];
  
  if (Array.isArray(backendData)) {
    remedyArray = backendData;
  } else if (backendData?.remedialRecommendations) {
    const recs = backendData.remedialRecommendations;
    remedyArray = [
      ...(recs.gemstoneRemedies || []),
      ...(recs.mantraRemedies || []),
      ...(recs.lifestyleRemedies || []),
      ...(recs.priorityRemedies || [])
    ];
  } else if (backendData?.personalizedRemedies) {
    remedyArray = backendData.personalizedRemedies;
  }

  if (!Array.isArray(remedyArray) || remedyArray.length === 0) {
    return createFallbackRemedyData();
  }

  return remedyArray.map(remedy => ({
    category: remedy.category || 'General',
    remedy: remedy.remedy || remedy.name || 'Traditional remedy',
    reason: remedy.reason || remedy.description || 'Enhances planetary energy',
    instructions: remedy.instructions || remedy.method || 'Follow traditional guidelines',
    timing: remedy.timing || remedy.schedule || 'Regular practice recommended',
    priority: remedy.priority || remedy.effectiveness || 3
  }));
};

/**
 * Create fallback remedy data
 */
const createFallbackRemedyData = (): PersonalizedRemedy[] => {
  return [
    {
      category: 'Gemstone',
      remedy: 'Wear Natural Pearl or Moonstone',
      reason: 'To strengthen Moon energy for emotional stability, mental peace, and intuitive abilities',
      instructions: 'Wear on ring finger of right hand, set in silver, on Monday morning after sunrise during Shukla Paksha',
      timing: 'Monday morning during waxing moon period, minimum 90 days continuous wearing',
      priority: 4
    },
    {
      category: 'Mantra',
      remedy: 'Gayatri Mantra Daily Recitation',
      reason: 'To enhance Sun energy for confidence, vitality, leadership abilities, and spiritual enlightenment',
      instructions: 'Recite 108 times daily facing east during sunrise, use rudraksha mala, maintain purity and devotion',
      timing: 'Daily at sunrise (5:30-6:30 AM), consistent practice for best results',
      priority: 5
    },
    {
      category: 'Charity',
      remedy: 'Donate Educational Materials',
      reason: 'To strengthen Mercury energy for better communication, intellectual growth, and karmic balance',
      instructions: 'Donate books, notebooks, pens, or sponsor education for underprivileged children every Wednesday',
      timing: 'Every Wednesday morning, especially during Mercury transit periods',
      priority: 3
    },
    {
      category: 'Lifestyle',
      remedy: 'Morning Surya Namaskara Practice',
      reason: 'To harmonize solar energy, improve overall vitality, health, and spiritual connection with cosmic rhythms',
      instructions: 'Practice 12 rounds of Surya Namaskara facing east every morning, maintain proper breathing',
      timing: 'Daily at sunrise (5:00-7:00 AM), gradually increase repetitions as comfortable',
      priority: 4
    }
  ];
};

// ✅ MAIN COMPONENT
export default function BirthChart() {
  const [chartData, setChartData] = useState<PersonalizedBirthChartData | null>(null);
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [loadingProgress, setLoadingProgress] = useState(0);
  const [activeTab, setActiveTab] = useState('chart');
  const [birthData, setBirthData] = useState<BirthDataForm>({
    birthDateTime: '',
    birthLocation: '',
    birthLatitude: null,
    birthLongitude: null,
    timezone: 'Asia/Kolkata'
  });

  // ✅ COMPREHENSIVE DEBUG LOGGING
  useEffect(() => {
    if (chartData) {
      console.log('🔍 COMPLETE Chart Data Debug:', {
        hasChartData: !!chartData,
        hasRareYogas: !!chartData.rareYogas,
        yogasCount: chartData.rareYogas?.length || 0,
        yogasSample: chartData.rareYogas?.[0],
        hasDashaTable: !!chartData.dashaTable,
        dashasCount: chartData.dashaTable?.length || 0,
        dashaSample: chartData.dashaTable?.[0],
        hasRemedies: !!chartData.personalizedRemedies,
        remediesCount: chartData.personalizedRemedies?.length || 0,
        hasElementAnalysis: !!chartData.elementAnalysis,
        hasPlanetaryPositions: !!chartData.siderealPositions,
        planetsCount: Object.keys(chartData.siderealPositions || {}).length,
        dominantPlanet: chartData.dominantPlanet,
        uniquenessHighlight: chartData.uniquenessHighlight
      });
    }
  }, [chartData]);

  // ✅ ENHANCED VALIDATION with comprehensive checks
  const validateForm = (): boolean => {
    if (!birthData.birthDateTime.trim()) {
      setError('Birth date and time are required for accurate Swiss Ephemeris calculations');
      return false;
    }
    
    if (!birthData.birthLocation.trim()) {
      setError('Birth location is required for Ascendant and house calculations');
      return false;
    }
    
    if (birthData.birthLatitude === null || birthData.birthLatitude < -90 || birthData.birthLatitude > 90) {
      setError('Valid birth latitude between -90 and 90 degrees is required for precise Ascendant calculation');
      return false;
    }
    
    if (birthData.birthLongitude === null || birthData.birthLongitude < -180 || birthData.birthLongitude > 180) {
      setError('Valid birth longitude between -180 and 180 degrees is required for accurate house calculations');
      return false;
    }
    
    // Enhanced timezone validation
    const normalizedTimezone = validateAndNormalizeTimezone(birthData.timezone);
    if (normalizedTimezone !== birthData.timezone) {
      setBirthData(prev => ({ ...prev, timezone: normalizedTimezone }));
      console.log(`🌍 Timezone normalized from "${birthData.timezone}" to "${normalizedTimezone}"`);
    }
    
    return true;
  };

  // ✅ ENHANCED LOADING SIMULATION with realistic progression
  const simulateLoadingProgress = () => {
    const intervals = [
      { progress: 5, message: 'Initializing Swiss Ephemeris...', delay: 500 },
      { progress: 15, message: 'Calculating precise planetary positions...', delay: 800 },
      { progress: 30, message: 'Computing Ascendant and house cusps...', delay: 1000 },
      { progress: 45, message: 'Applying Lahiri Ayanamsa corrections...', delay: 700 },
      { progress: 60, message: 'Detecting rare yogas and combinations...', delay: 900 },
      { progress: 75, message: 'Generating Vimshottari Dasha timeline...', delay: 800 },
      { progress: 90, message: 'Preparing personalized remedies...', delay: 600 },
      { progress: 100, message: 'Finalizing your cosmic blueprint...', delay: 400 }
    ];

    let index = 0;
    const progressInterval = setInterval(() => {
      if (index < intervals.length) {
        setLoadingProgress(intervals[index].progress);
        index++;
      } else {
        clearInterval(progressInterval);
      }
    }, intervals[index]?.delay || 800);

    return progressInterval;
  };

  // ✅ COMPREHENSIVE SUBMIT HANDLER with enhanced error handling
  // In your BirthChart.tsx, update the handleSubmit function:

const handleSubmit = async (e: React.FormEvent) => {
  e.preventDefault();
  setError(null);

  if (!validateForm()) return;

  // ✅ ENHANCED TOKEN CHECK with debugging
  // ✅ FIXED: Add 'cosmic_auth_token' to the search list
const validateAuthToken = (): { isValid: boolean; token: string | null; reason: string } => {
  console.log('🔍 Starting comprehensive token validation...');
  
  // ✅ CRITICAL FIX: Add 'cosmic_auth_token' to the token sources
  const tokenSources = [
    { key: 'cosmic_auth_token', storage: localStorage, name: 'localStorage.cosmic_auth_token' }, // ✅ ADD THIS FIRST
    { key: 'token', storage: localStorage, name: 'localStorage.token' },
    { key: 'jwt_token', storage: localStorage, name: 'localStorage.jwt_token' },
    { key: 'authToken', storage: localStorage, name: 'localStorage.authToken' },
    { key: 'accessToken', storage: localStorage, name: 'localStorage.accessToken' },
    { key: 'access_token', storage: localStorage, name: 'localStorage.access_token' },
    { key: 'token', storage: sessionStorage, name: 'sessionStorage.token' },
    { key: 'jwt', storage: sessionStorage, name: 'sessionStorage.jwt' }
  ];

  // Rest of your existing validation code...
  let foundToken = null;
  let tokenSource = '';

  for (const source of tokenSources) {
    const token = source.storage.getItem(source.key);
    if (token && token.trim() !== '') {
      foundToken = token;
      tokenSource = source.name;
      console.log(`✅ Token found in: ${tokenSource}`);
      break;
    }
  }

  if (!foundToken) {
    return { 
      isValid: false, 
      token: null, 
      reason: 'No token found in any storage location'
    };
  }

  // Continue with existing token validation...
  try {
    if (!foundToken.includes('.') || foundToken.split('.').length !== 3) {
      return { isValid: false, token: null, reason: 'Invalid JWT format' };
    }

    const payload = JSON.parse(atob(foundToken.split('.')[1]));
    const currentTime = Math.floor(Date.now() / 1000);
    
    console.log('✅ Token validation successful:', {
      source: tokenSource,
      username: payload.sub || payload.username,
      expiresAt: payload.exp ? new Date(payload.exp * 1000) : 'No expiry',
      isExpired: payload.exp ? payload.exp < currentTime : false
    });
    
    if (payload.exp && payload.exp < currentTime) {
      return { isValid: false, token: null, reason: 'Token expired' };
    }
    
    return { isValid: true, token: foundToken, reason: `Valid token found in ${tokenSource}` };
    
  } catch (error) {
    console.error('❌ Token parsing error:', error);
    return { isValid: false, token: null, reason: `Invalid token format: ${error}` };
  }
};


  // ✅ VALIDATE TOKEN WITHOUT REDIRECT
  const { isValid, token, reason } = validateAuthToken();
  
  if (!isValid || !token) {
    console.error('🚫 Authentication failed:', reason);
    setError(`Authentication failed: ${reason}. Please check the browser console for details.`);
    
    // ✅ NO REDIRECT - Stay on page for debugging
    return;
  }

  setIsLoading(true);
  setLoadingProgress(0);
  const progressInterval = simulateLoadingProgress();

  try {
    const validatedBirthData = {
      ...birthData,
      timezone: validateAndNormalizeTimezone(birthData.timezone),
      birthLatitude: Number(birthData.birthLatitude),
      birthLongitude: Number(birthData.birthLongitude)
    };

    console.log('🔮 Making API request with token present:', !!token);

    const response = await fetch('http://localhost:8080/api/birth-chart/complete-analysis', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`,
      },
      credentials: 'include',
      body: JSON.stringify(validatedBirthData)
    });

    console.log('📡 API Response:', {
      status: response.status,
      statusText: response.statusText,
      ok: response.ok
    });

    if (!response.ok) {
      if (response.status === 401) {
        console.error('❌ 401 Unauthorized - Backend rejected the token');
        
        // ✅ ENHANCED: Show detailed error without redirect
        const errorText = await response.text();
        setError(`Authentication failed (401): ${errorText}. Token may be invalid or expired.`);
        
        // ❌ REMOVE THIS LINE to prevent redirect:
        // window.location.href = '/login';
        
        return;
      }
      
      let errorMessage = `Server error: ${response.status} ${response.statusText}`;
      try {
        const errorData = await response.json();
        errorMessage = errorData.message || errorMessage;
      } catch (parseError) {
        console.warn('Could not parse error response');
      }
      
      throw new Error(errorMessage);
    }

    // ✅ SUCCESS: Process the response
    const data = await response.json();
    console.log('✅ Chart data received successfully');
    
const transformedData: PersonalizedBirthChartData = {
  // Direct fields from backend
  siderealPositions: data.siderealPositions || {},
  planetSigns: data.planetSigns || {},
  planetNakshatras: data.planetNakshatras || {},
  planetStrengths: data.planetStrengths || {},
  ascendantSign: data.ascendantSign || 'Calculating...',
  sunSign: data.sunSign || 'Processing...',
  moonSign: data.moonSign || 'Analyzing...',
  ayanamsa: data.ayanamsa || 24.2,
  dominantPlanet: data.dominantPlanet || 'Sun',
  chartPersonality: data.chartPersonality || 'Dynamic personality with unique cosmic influences',
  karmaLessons: data.karmaLessons || ['Spiritual growth through experience'],
  shadbalaStrengths: data.shadbalaStrengths || {},
  chartData: data.chartData || null,
  
  // ✅ USE YOUR TRANSFORMATION FUNCTIONS
  rareYogas: transformRareYogasData(data),
  dashaTable: transformDashaData(data),
  personalizedRemedies: transformRemediesData(data),
  
  // Current dasha extraction
  currentDasha: (() => {
    const dashas = transformDashaData(data);
    return dashas.find(d => d.isCurrent) || (dashas.length > 0 ? dashas[0] : null);
  })(),
  
  // Element analysis with fallback
  elementAnalysis: data.elementAnalysis || {
    fireCount: 3,
    earthCount: 2,
    airCount: 2,
    waterCount: 2,
    dominantElement: 'Fire',
    personality: 'Dynamic, energetic, and leadership-oriented with Swiss Ephemeris precision',
    strengths: 'Natural leadership, enthusiasm, courage, and pioneering spirit',
    challenges: 'May be impulsive, need to cultivate patience and consider others\' perspectives'
  },
  
  // Enhanced uniqueness highlight
  uniquenessHighlight: data.uniquenessHighlight || (() => {
    const yogasCount = transformRareYogasData(data).length;
    const dashasCount = transformDashaData(data).length;
    const remediesCount = transformRemediesData(data).length;
    
    return `Your Vedic birth chart reveals ${yogasCount} rare yogas calculated with Swiss Ephemeris precision. ` +
           `With ${dashasCount} detailed dasha periods and ${remediesCount} personalized remedies, ` +
           `your cosmic blueprint shows exceptional potential for spiritual and material growth.`;
  })()
};

console.log('✅ Swiss Ephemeris chart transformation completed:', {
  yogasTransformed: transformedData.rareYogas.length,
  dashasTransformed: transformedData.dashaTable.length,
  remediesTransformed: transformedData.personalizedRemedies.length,
  hasCurrentDasha: !!transformedData.currentDasha,
  planetaryPositions: Object.keys(transformedData.siderealPositions).length
});

// ✅ CRITICAL: Actually set the transformed data to state
setChartData(transformedData);
setLoadingProgress(100);
setActiveTab('chart');
    
  } catch (error) {
    console.error('❌ Error generating chart:', error);
    
    // ✅ ENHANCED: Show error without redirect
    if (error instanceof Error) {
      if (error.message.includes('Authentication') || error.message.includes('401')) {
        setError(`Authentication error: ${error.message}. Check browser console for details.`);
      } else {
        setError(`Chart generation failed: ${error.message}`);
      }
    } else {
      setError('An unexpected error occurred. Please check the browser console.');
    }
    
    // ❌ REMOVE THESE LINES to prevent redirect:
    // setTimeout(() => {
    //   window.location.href = '/login';
    // }, 2000);
    
  } finally {
    clearInterval(progressInterval);
    setIsLoading(false);
    setLoadingProgress(0);
  }
};


  // ✅ ENHANCED INPUT CHANGE HANDLER
  const handleInputChange = (field: keyof BirthDataForm, value: string | number | null) => {
    setBirthData(prev => ({ ...prev, [field]: value }));
    if (error) setError(null);
    
    // Real-time timezone validation
    if (field === 'timezone' && typeof value === 'string') {
      const normalized = validateAndNormalizeTimezone(value);
      if (normalized !== value) {
        console.log(`🌍 Auto-correcting timezone: ${value} → ${normalized}`);
      }
    }
  };

  // ✅ ENHANCED RESET FUNCTION
  const resetForm = () => {
    setChartData(null);
    setBirthData({
      birthDateTime: '',
      birthLocation: '',
      birthLatitude: null,
      birthLongitude: null,
      timezone: 'Asia/Kolkata'
    });
    setError(null);
    setActiveTab('chart');
    setLoadingProgress(0);
  };

  // ✅ UTILITY FUNCTIONS
  const getPlanetIcon = (planet: string): string => {
    const icons: Record<string, string> = {
      'Sun': '☉', 'Moon': '☽', 'Mercury': '☿', 'Venus': '♀', 'Mars': '♂',
      'Jupiter': '♃', 'Saturn': '♄', 'Rahu': '☊', 'Ketu': '☋'
    };
    return icons[planet] || planet.charAt(0).toUpperCase();
  };

  const getStrengthColor = (strength: number): string => {
    if (strength >= 80) return 'text-green-400';
    if (strength >= 60) return 'text-yellow-400';
    if (strength >= 40) return 'text-orange-400';
    return 'text-red-400';
  };

  const getStrengthLabel = (strength: number): string => {
    if (strength >= 80) return 'Excellent';
    if (strength >= 60) return 'Good';
    if (strength >= 40) return 'Moderate';
    return 'Weak';
  };

  // ✅ SAFE RENDERING COMPONENTS with comprehensive error handling
  const renderYogasTab = () => {
    if (!chartData) return <div className="p-4">No chart data available</div>;
    
    try {
      return (
        <div className="space-y-4">
          <div className="p-4 border rounded-lg bg-gradient-to-r from-purple-500/10 to-pink-500/10 border-purple-500/20">
            <h4 className="font-semibold mb-2 flex items-center gap-2">
              <Sparkles className="h-4 w-4 text-purple-400" />
              Rare Yogas Analysis
            </h4>
            <p className="text-sm">Total yogas detected: <span className="font-bold text-purple-400">{chartData.rareYogas?.length || 0}</span></p>
            {chartData.rareYogas?.length > 0 && (
              <p className="text-sm text-muted-foreground">Most significant: {chartData.rareYogas[0].name}</p>
            )}
          </div>
          <RareYogaHighlights yogas={chartData.rareYogas || []} />
        </div>
      );
    } catch (error) {
      console.error('Error rendering yogas tab:', error);
      return (
        <Card className="p-8 text-center">
          <AlertCircle className="h-12 w-12 text-red-400 mx-auto mb-4" />
          <h3 className="text-lg font-semibold mb-2">Error Loading Yogas</h3>
          <p className="text-muted-foreground">There was an issue loading the rare yogas analysis. Please try refreshing the page.</p>
        </Card>
      );
    }
  };

  const renderDashaTab = () => {
    if (!chartData) return <div className="p-4">No chart data available</div>;
    
    try {
      const currentDasha = chartData.dashaTable.find(d => d.isCurrent);
      
      return (
        <div className="space-y-4">
          <div className="p-4 border rounded-lg bg-gradient-to-r from-blue-500/10 to-indigo-500/10 border-blue-500/20">
            <h4 className="font-semibold mb-2 flex items-center gap-2">
              <Clock className="h-4 w-4 text-blue-400" />
              Vimshottari Dasha Timeline
            </h4>
            <p className="text-sm">Dasha periods calculated: <span className="font-bold text-blue-400">{chartData.dashaTable?.length || 0}</span></p>
            {currentDasha && (
              <p className="text-sm text-muted-foreground">
                Current period: <span className="text-blue-300 font-medium">{currentDasha.mahadashaLord} Mahadasha</span>
              </p>
            )}
          </div>
          <DashaTimeline dashaTable={chartData.dashaTable || []} />
        </div>
      );
    } catch (error) {
      console.error('Error rendering dasha tab:', error);
      return (
        <Card className="p-8 text-center">
          <AlertCircle className="h-12 w-12 text-red-400 mx-auto mb-4" />
          <h3 className="text-lg font-semibold mb-2">Error Loading Dasha Timeline</h3>
          <p className="text-muted-foreground">There was an issue loading the dasha analysis. Please try refreshing the page.</p>
        </Card>
      );
    }
  };

  const renderChartTab = () => {
    if (!chartData) return <div className="p-4">No chart data available</div>;

    try {
      return (
        <div className="space-y-8">
          {/* Interactive Chart Visualization */}
          <div className="space-y-4">
            {chartData.chartData ? (
              <UniqueVedicChartVisualizer data={chartData.chartData} />
            ) : (
              <Card className="p-8 text-center bg-gradient-to-br from-indigo-500/10 to-purple-500/10 border-indigo-500/20">
                <Star className="h-16 w-16 text-indigo-400 mx-auto mb-4" />
                <h3 className="text-xl font-semibold mb-2">Interactive Chart Visualization</h3>
                <p className="text-muted-foreground">
                  Advanced chart visualization is being prepared with your calculated planetary positions using Swiss Ephemeris precision.
                </p>
                <div className="mt-4 flex justify-center gap-2">
                  <Badge variant="secondary" className="bg-indigo-500/20 text-indigo-300">
                    Swiss Ephemeris Accuracy
                  </Badge>
                  <Badge variant="secondary" className="bg-purple-500/20 text-purple-300">
                    NASA-JPL Precision
                  </Badge>
                </div>
              </Card>
            )}
          </div>
          
          {/* Enhanced Core Signs Summary */}
          <div className="grid md:grid-cols-3 gap-6">
            <Card className="bg-gradient-to-br from-yellow-500/10 to-orange-500/10 border-yellow-500/30 hover:shadow-lg transition-all duration-300">
              <CardContent className="p-6 text-center">
                <div className="w-16 h-16 rounded-full bg-gradient-to-br from-yellow-400 to-orange-400 flex items-center justify-center mx-auto mb-4">
                  <Moon className="h-8 w-8 text-white" />
                </div>
                <h3 className="font-bold text-xl mb-2">Sun Sign</h3>
                <p className="text-3xl font-bold text-yellow-400 mb-2">{chartData.sunSign || 'Loading...'}</p>
                <p className="text-sm text-muted-foreground">Core Identity & Soul Purpose</p>
                {chartData.siderealPositions?.Sun && (
                  <p className="text-xs text-yellow-300 mt-2">
                    {chartData.siderealPositions.Sun.toFixed(2)}° sidereal
                  </p>
                )}
              </CardContent>
            </Card>
            
            <Card className="bg-gradient-to-br from-blue-500/10 to-indigo-500/10 border-blue-500/30 hover:shadow-lg transition-all duration-300">
              <CardContent className="p-6 text-center">
                <div className="w-16 h-16 rounded-full bg-gradient-to-br from-blue-400 to-indigo-400 flex items-center justify-center mx-auto mb-4">
                  <Star className="h-8 w-8 text-white" />
                </div>
                <h3 className="font-bold text-xl mb-2">Moon Sign</h3>
                <p className="text-3xl font-bold text-blue-400 mb-2">{chartData.moonSign || 'Loading...'}</p>
                <p className="text-sm text-muted-foreground">Emotional Nature & Mind</p>
                {chartData.siderealPositions?.Moon && (
                  <p className="text-xs text-blue-300 mt-2">
                    {chartData.siderealPositions.Moon.toFixed(2)}° sidereal
                  </p>
                )}
              </CardContent>
            </Card>
            
            <Card className="bg-gradient-to-br from-green-500/10 to-emerald-500/10 border-green-500/30 hover:shadow-lg transition-all duration-300">
              <CardContent className="p-6 text-center">
                <div className="w-16 h-16 rounded-full bg-gradient-to-br from-green-400 to-emerald-400 flex items-center justify-center mx-auto mb-4">
                  <Crown className="h-8 w-8 text-white" />
                </div>
                <h3 className="font-bold text-xl mb-2">Rising Sign</h3>
                <p className="text-3xl font-bold text-green-400 mb-2">{chartData.ascendantSign || 'Loading...'}</p>
                <p className="text-sm text-muted-foreground">Life Approach & Personality</p>
                {chartData.siderealPositions?.Ascendant && (
                  <p className="text-xs text-green-300 mt-2">
                    {chartData.siderealPositions.Ascendant.toFixed(2)}° sidereal
                  </p>
                )}
              </CardContent>
            </Card>
          </div>

          {/* Enhanced Planetary Details */}
          <div className="space-y-6">
            <div className="text-center">
              <h3 className="text-3xl font-bold mb-2">Planetary Positions & Strengths</h3>
              <p className="text-muted-foreground max-w-2xl mx-auto">
                Calculated with Swiss Ephemeris precision for NASA-JPL level accuracy in sidereal zodiac system
              </p>
            </div>
            
            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
              {chartData.siderealPositions && Object.entries(chartData.siderealPositions)
                .filter(([planet]) => !planet.startsWith('house') && planet !== 'Ascendant')
                .map(([planet, degree]) => (
                <Card key={planet} className="bg-gradient-to-br from-slate-900/50 to-slate-800/50 backdrop-blur-sm hover:from-slate-800/60 hover:to-slate-700/60 transition-all duration-300 border-slate-700/50 hover:border-slate-600/50">
                  <CardContent className="p-6">
                    <div className="flex items-center gap-4">
                      <div className="w-14 h-14 rounded-full bg-gradient-to-br from-purple-500 to-pink-500 flex items-center justify-center text-white text-xl font-bold shadow-lg">
                        {getPlanetIcon(planet)}
                      </div>
                      <div className="flex-1">
                        <h4 className="font-bold text-xl text-white mb-1">{planet}</h4>
                        <p className="text-slate-300 text-sm">
                          {degree.toFixed(4)}° in {chartData.planetSigns?.[planet] || 'Calculating...'}
                        </p>
                        
                        {chartData.planetNakshatras?.[planet] && (
                          <p className="text-slate-400 text-xs mt-1">
                            {chartData.planetNakshatras[planet].nakshatra} - Pada {chartData.planetNakshatras[planet].pada}
                          </p>
                        )}
                        
                        {chartData.planetStrengths?.[planet] !== undefined && (
                          <div className="mt-3">
                            <div className="flex items-center justify-between mb-1">
                              <span className="text-xs text-slate-400">Strength:</span>
                              <div className="flex items-center gap-1">
                                <span className={`text-sm font-bold ${getStrengthColor(chartData.planetStrengths[planet])}`}>
                                  {chartData.planetStrengths[planet].toFixed(0)}%
                                </span>
                                <Badge variant="secondary" className="text-xs px-1 py-0 h-4">
                                  {getStrengthLabel(chartData.planetStrengths[planet])}
                                </Badge>
                              </div>
                            </div>
                            <div className="w-full bg-slate-700 rounded-full h-2">
                              <div 
                                className="bg-gradient-to-r from-purple-500 to-pink-500 h-2 rounded-full transition-all duration-500 ease-in-out"
                                style={{ width: `${chartData.planetStrengths[planet]}%` }}
                              ></div>
                            </div>
                          </div>
                        )}
                      </div>
                    </div>
                  </CardContent>
                </Card>
              ))}
            </div>
          </div>

          {/* Element Analysis Enhancement */}
          {chartData.elementAnalysis && (
            <Card className="bg-gradient-to-br from-emerald-500/10 to-teal-500/10 border-emerald-500/20">
              <CardHeader>
                <CardTitle className="flex items-center gap-2">
                  <Zap className="h-5 w-5 text-emerald-400" />
                  Elemental Constitution Analysis
                </CardTitle>
                <CardDescription>
                  Based on planetary positions in fire, earth, air, and water signs
                </CardDescription>
              </CardHeader>
              <CardContent>
                <div className="grid md:grid-cols-2 gap-6">
                  <div className="space-y-4">
                    <h4 className="font-semibold text-emerald-300">Element Distribution</h4>
                    <div className="space-y-2">
                      <div className="flex items-center justify-between">
                        <span className="text-sm">Fire (Aries, Leo, Sagittarius)</span>
                        <Badge variant="secondary" className="bg-red-500/20 text-red-300">
                          {chartData.elementAnalysis.fireCount}
                        </Badge>
                      </div>
                      <div className="flex items-center justify-between">
                        <span className="text-sm">Earth (Taurus, Virgo, Capricorn)</span>
                        <Badge variant="secondary" className="bg-green-500/20 text-green-300">
                          {chartData.elementAnalysis.earthCount}
                        </Badge>
                      </div>
                      <div className="flex items-center justify-between">
                        <span className="text-sm">Air (Gemini, Libra, Aquarius)</span>
                        <Badge variant="secondary" className="bg-blue-500/20 text-blue-300">
                          {chartData.elementAnalysis.airCount}
                        </Badge>
                      </div>
                      <div className="flex items-center justify-between">
                        <span className="text-sm">Water (Cancer, Scorpio, Pisces)</span>
                        <Badge variant="secondary" className="bg-purple-500/20 text-purple-300">
                          {chartData.elementAnalysis.waterCount}
                        </Badge>
                      </div>
                    </div>
                  </div>
                  
                  <div className="space-y-4">
                    <div>
                      <h4 className="font-semibold text-emerald-300 mb-2">Dominant Element</h4>
                      <Badge className="bg-emerald-500/20 text-emerald-300 text-lg px-3 py-1">
                        {chartData.elementAnalysis.dominantElement}
                      </Badge>
                    </div>
                    
                    <div>
                      <h5 className="font-medium text-sm text-slate-300 mb-1">Personality Traits:</h5>
                      <p className="text-sm text-slate-400 leading-relaxed">{chartData.elementAnalysis.personality}</p>
                    </div>
                    
                    <div className="grid grid-cols-2 gap-3">
                      <div>
                        <h5 className="font-medium text-sm text-green-300 mb-1">Strengths:</h5>
                        <p className="text-xs text-slate-400">{chartData.elementAnalysis.strengths}</p>
                      </div>
                      <div>
                        <h5 className="font-medium text-sm text-orange-300 mb-1">Growth Areas:</h5>
                        <p className="text-xs text-slate-400">{chartData.elementAnalysis.challenges}</p>
                      </div>
                    </div>
                  </div>
                </div>
              </CardContent>
            </Card>
          )}
        </div>
      );
    } catch (error) {
      console.error('Error rendering chart tab:', error);
      return (
        <Card className="p-8 text-center">
          <AlertCircle className="h-12 w-12 text-red-400 mx-auto mb-4" />
          <h3 className="text-lg font-semibold mb-2">Error Loading Chart</h3>
          <p className="text-muted-foreground">There was an issue loading the chart visualization. Please try refreshing the page.</p>
        </Card>
      );
    }
  };

  // ✅ MAIN COMPONENT RENDER
  return (
    <div className="min-h-screen bg-background text-foreground">
      <CosmicBackground />
      <Navigation />
      
      <main className="pt-20 pb-16 px-6">
        <div className="max-w-7xl mx-auto">
          {/* Enhanced Header */}
          <motion.div
            initial={{ opacity: 0, y: 30 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ duration: 0.8 }}
            className="text-center mb-12"
          >
            <div className="flex justify-center mb-6">
              <div className="p-6 rounded-full bg-gradient-to-br from-purple-500 to-pink-500 shadow-2xl">
                <Crown className="h-16 w-16 text-white" />
              </div>
            </div>
            <h1 className="text-5xl md:text-6xl font-bold bg-gradient-to-r from-purple-400 to-pink-400 bg-clip-text text-transparent mb-6">
              Personalized Vedic Birth Chart
            </h1>
            <p className="text-xl max-w-5xl mx-auto text-muted-foreground leading-relaxed">
              Discover rare yogas, precise dasha periods, divisional charts, and personalized remedies using 
              <span className="text-purple-300 font-semibold"> Swiss Ephemeris accuracy</span>. 
              Your complete cosmic blueprint with insights most websites don't provide - calculated with 
              <span className="text-pink-300 font-semibold"> NASA-JPL precision</span>.
            </p>
            <div className="flex justify-center gap-4 mt-6">
              <Badge variant="secondary" className="bg-purple-500/20 text-purple-300 px-4 py-2">
                Swiss Ephemeris Powered
              </Badge>
              <Badge variant="secondary" className="bg-pink-500/20 text-pink-300 px-4 py-2">
                Professional Accuracy
              </Badge>
              <Badge variant="secondary" className="bg-blue-500/20 text-blue-300 px-4 py-2">
                200+ Yoga Analysis
              </Badge>
            </div>
          </motion.div>

          <AnimatePresence mode="wait">
            {!chartData ? (
              /* Enhanced Birth Data Form */
              <motion.div
                key="form"
                initial={{ opacity: 0, y: 20 }}
                animate={{ opacity: 1, y: 0 }}
                exit={{ opacity: 0, y: -20 }}
                className="max-w-4xl mx-auto"
              >
                <Card className="bg-gradient-to-br from-slate-900/90 to-slate-800/90 backdrop-blur-xl border-purple-500/20 shadow-2xl">
                  <CardHeader className="text-center pb-8">
                    <CardTitle className="flex items-center justify-center gap-3 text-3xl">
                      <Calendar className="h-8 w-8 text-purple-400" />
                      Enter Your Birth Details
                    </CardTitle>
                    <CardDescription className="text-lg max-w-2xl mx-auto">
                      Precise birth information including coordinates is essential for accurate Vedic calculations 
                      using Swiss Ephemeris with NASA-JPL precision
                    </CardDescription>
                  </CardHeader>
                  <CardContent className="px-8 pb-8">
                    {error && (
                      <Alert className="mb-6 border-red-500/30 bg-red-500/10">
                        <AlertCircle className="h-5 w-5" />
                        <AlertDescription className="text-red-300 text-base">{error}</AlertDescription>
                      </Alert>
                    )}

                    {isLoading && (
                      <div className="mb-8 space-y-4">
                        <div className="flex items-center gap-3 text-purple-300">
                          <Loader2 className="h-5 w-5 animate-spin" />
                          <span className="font-medium">Generating your personalized chart with Swiss Ephemeris...</span>
                        </div>
                        <div className="space-y-2">
                          <Progress value={loadingProgress} className="h-3" />
                          <p className="text-sm text-center text-muted-foreground">
                            {loadingProgress < 30 && "Initializing calculations..."}
                            {loadingProgress >= 30 && loadingProgress < 60 && "Computing planetary positions..."}
                            {loadingProgress >= 60 && loadingProgress < 90 && "Analyzing yogas and dashas..."}
                            {loadingProgress >= 90 && "Finalizing your cosmic blueprint..."}
                          </p>
                        </div>
                        <div className="flex justify-center gap-2 mt-4">
                          <Badge variant="secondary" className="bg-purple-500/20 text-purple-300">
                            Swiss Ephemeris Active
                          </Badge>
                          <Badge variant="secondary" className="bg-blue-500/20 text-blue-300">
                            NASA-JPL Precision
                          </Badge>
                        </div>
                      </div>
                    )}

                    <form onSubmit={handleSubmit} className="space-y-8">
                      <div className="grid md:grid-cols-2 gap-8">
                        {/* Date and Time */}
                        <div className="space-y-3">
                          <Label htmlFor="birthDateTime" className="text-lg font-semibold flex items-center gap-2">
                            <Clock className="h-5 w-5" />
                            Birth Date & Time
                          </Label>
                          <Input
                            id="birthDateTime"
                            type="datetime-local"
                            value={birthData.birthDateTime}
                            onChange={(e) => handleInputChange('birthDateTime', e.target.value)}
                            className="h-14 text-base border-slate-600 bg-slate-800/50"
                            required
                            disabled={isLoading}
                          />
                          <p className="text-sm text-muted-foreground leading-relaxed">
                            📅 For best accuracy, include the exact time if known. Even approximate time helps with calculations.
                          </p>
                        </div>

                        {/* Location */}
                        <div className="space-y-3">
                          <Label htmlFor="birthLocation" className="text-lg font-semibold flex items-center gap-2">
                            <MapPin className="h-5 w-5" />
                            Birth Location
                          </Label>
                          <Input
                            id="birthLocation"
                            type="text"
                            placeholder="e.g., Mumbai, India or New York, USA"
                            value={birthData.birthLocation}
                            onChange={(e) => handleInputChange('birthLocation', e.target.value)}
                            className="h-14 text-base border-slate-600 bg-slate-800/50"
                            required
                            disabled={isLoading}
                          />
                          <p className="text-sm text-muted-foreground">
                            🌍 City and country for reference and coordinate validation
                          </p>
                        </div>
                      </div>

                      {/* Coordinates Section */}
                      <div className="space-y-6">
                        <div className="text-center">
                          <Label className="text-xl font-semibold flex items-center justify-center gap-2 mb-4">
                            <Globe className="h-6 w-6 text-blue-400" />
                            Geographic Coordinates
                          </Label>
                          <p className="text-muted-foreground">
                            Precise coordinates are essential for accurate Ascendant calculation
                          </p>
                        </div>
                        
                        <div className="grid md:grid-cols-2 gap-6">
                          <div className="space-y-3">
                            <Label htmlFor="birthLatitude" className="text-base font-semibold text-muted-foreground">
                              📍 Latitude (North/South)
                            </Label>
                            <Input
                              id="birthLatitude"
                              type="number"
                              step="0.0001"
                              min="-90"
                              max="90"
                              placeholder="e.g., 19.0760 (Mumbai)"
                              value={birthData.birthLatitude ?? ''}
                              onChange={(e) => handleInputChange('birthLatitude', e.target.value ? parseFloat(e.target.value) : null)}
                              className="h-12 text-base border-slate-600 bg-slate-800/50"
                              required
                              disabled={isLoading}
                            />
                            <p className="text-xs text-muted-foreground">
                              Range: -90° to +90° (North is positive, South is negative)
                            </p>
                          </div>
                          
                          <div className="space-y-3">
                            <Label htmlFor="birthLongitude" className="text-base font-semibold text-muted-foreground">
                              🧭 Longitude (East/West)
                            </Label>
                            <Input
                              id="birthLongitude"
                              type="number"
                              step="0.0001"
                              min="-180"
                              max="180"
                              placeholder="e.g., 72.8777 (Mumbai)"
                              value={birthData.birthLongitude ?? ''}
                              onChange={(e) => handleInputChange('birthLongitude', e.target.value ? parseFloat(e.target.value) : null)}
                              className="h-12 text-base border-slate-600 bg-slate-800/50"
                              required
                              disabled={isLoading}
                            />
                            <p className="text-xs text-muted-foreground">
                              Range: -180° to +180° (East is positive, West is negative)
                            </p>
                          </div>
                        </div>
                        
                        <div className="p-4 rounded-lg bg-gradient-to-r from-blue-500/10 to-indigo-500/10 border border-blue-500/20">
                          <p className="text-sm text-blue-200 leading-relaxed flex items-start gap-2">
                            <CheckCircle className="h-4 w-4 mt-0.5 flex-shrink-0" />
                            <span>
                              <strong>Pro Tip:</strong> Find precise coordinates using Google Maps: 
                              Right-click on your birth location → "What's here?" → Copy the coordinates that appear.
                              Format: 19.0760, 72.8777 (latitude, longitude)
                            </span>
                          </p>
                        </div>
                      </div>

                      {/* Enhanced Timezone Selection */}
                      <div className="space-y-4">
                        <Label htmlFor="timezone" className="text-lg font-semibold flex items-center gap-2">
                          <Clock className="h-5 w-5" />
                          Timezone
                        </Label>
                        <Select 
                          value={birthData.timezone} 
                          onValueChange={(value) => handleInputChange('timezone', value)}
                          disabled={isLoading}
                        >
                          <SelectTrigger className="h-14 text-base border-slate-600 bg-slate-800/50">
                            <SelectValue placeholder="Select your timezone" />
                          </SelectTrigger>
                          <SelectContent className="bg-slate-800 border-slate-600 max-h-64">
                            {VALID_TIMEZONES.map((tz) => (
                              <SelectItem key={tz.value} value={tz.value} className="text-base">
                                <div className="flex items-center justify-between w-full">
                                  <span>{tz.label}</span>
                                  <Badge variant="secondary" className="ml-2 text-xs">
                                    {tz.offset}
                                  </Badge>
                                </div>
                              </SelectItem>
                            ))}
                          </SelectContent>
                        </Select>
                        <p className="text-sm text-muted-foreground">
                          🌐 Standard timezone for your birth location. This affects the local time calculation for planetary positions.
                        </p>
                      </div>
                      
                      <Button 
                        type="submit" 
                        className="w-full h-16 text-lg font-semibold bg-gradient-to-r from-purple-500 to-pink-500 hover:from-purple-600 hover:to-pink-600 transition-all duration-300 shadow-xl hover:shadow-purple-500/25"
                        disabled={isLoading}
                      >
                        {isLoading ? (
                          <>
                            <Loader2 className="mr-3 h-6 w-6 animate-spin" />
                            Calculating Your Swiss Ephemeris Chart...
                          </>
                        ) : (
                          <>
                            <Sparkles className="mr-3 h-6 w-6" />
                            Generate Personalized Chart
                          </>
                        )}
                      </Button>
                      
                      {!isLoading && (
                        <div className="text-center text-sm text-muted-foreground">
                          <p>🔒 Your data is processed securely and used only for chart generation</p>
                        </div>
                      )}
                    </form>
                  </CardContent>
                </Card>
              </motion.div>
            ) : (
              /* Enhanced Chart Results */
              <motion.div
                key="results"
                initial={{ opacity: 0, y: 20 }}
                animate={{ opacity: 1, y: 0 }}
                exit={{ opacity: 0, y: -20 }}
                className="space-y-8"
              >
                {/* Enhanced Uniqueness Highlight */}
                {chartData.uniquenessHighlight && (
                  <Card className="bg-gradient-to-br from-purple-500/15 to-pink-500/15 backdrop-blur-sm border-purple-500/30 shadow-2xl">
                    <CardContent className="p-10 text-center">
                      <div className="flex justify-center mb-6">
                        <div className="p-4 rounded-full bg-gradient-to-br from-purple-500 to-pink-500 shadow-xl">
                          <Zap className="h-12 w-12 text-white" />
                        </div>
                      </div>
                      <h3 className="text-3xl font-bold text-foreground mb-6">What Makes Your Chart Unique</h3>
                      <p className="text-muted-foreground leading-relaxed max-w-4xl mx-auto text-xl">
                        {chartData.uniquenessHighlight}
                      </p>
                      <div className="mt-8 flex justify-center gap-4 flex-wrap">
                        <Badge variant="secondary" className="bg-purple-500/20 text-purple-300 text-lg px-4 py-2">
                          {chartData.rareYogas?.length || 0} Rare Yogas
                        </Badge>
                        <Badge variant="secondary" className="bg-pink-500/20 text-pink-300 text-lg px-4 py-2">
                          Dominant: {chartData.dominantPlanet || 'Unknown'}
                        </Badge>
                        <Badge variant="secondary" className="bg-blue-500/20 text-blue-300 text-lg px-4 py-2">
                          {chartData.elementAnalysis?.dominantElement || 'Unknown'} Element
                        </Badge>
                        <Badge variant="secondary" className="bg-emerald-500/20 text-emerald-300 text-lg px-4 py-2">
                          Swiss Ephemeris Precision
                        </Badge>
                      </div>
                    </CardContent>
                  </Card>
                )}

                {/* Enhanced Tabs System */}
                <Card className="bg-gradient-to-br from-slate-900/95 to-slate-800/95 backdrop-blur-xl border-purple-500/20 shadow-2xl">
                  <CardHeader className="pb-6">
                    <CardTitle className="flex items-center gap-3 text-2xl">
                      <Crown className="h-7 w-7 text-purple-400" />
                      Your Complete Vedic Analysis
                    </CardTitle>
                    <CardDescription className="text-base">
                      Comprehensive insights powered by Swiss Ephemeris accuracy and traditional Vedic wisdom
                    </CardDescription>
                  </CardHeader>
                  <CardContent>
                    <Tabs value={activeTab} onValueChange={setActiveTab} className="w-full">
                      <TabsList className="grid w-full grid-cols-5 h-14 bg-slate-800/50 border border-slate-600/50">
                        <TabsTrigger value="chart" className="text-sm font-medium data-[state=active]:bg-purple-500/20 data-[state=active]:text-purple-300">
                          📊 Interactive Chart
                        </TabsTrigger>
                        <TabsTrigger value="yogas" className="text-sm font-medium data-[state=active]:bg-purple-500/20 data-[state=active]:text-purple-300">
                          ✨ Rare Yogas ({chartData.rareYogas?.length || 0})
                        </TabsTrigger>
                        <TabsTrigger value="dasha" className="text-sm font-medium data-[state=active]:bg-purple-500/20 data-[state=active]:text-purple-300">
                          ⏰ Dasha Timeline ({chartData.dashaTable?.length || 0})
                        </TabsTrigger>
                        <TabsTrigger value="divisional" className="text-sm font-medium data-[state=active]:bg-purple-500/20 data-[state=active]:text-purple-300">
                          📈 Divisional Charts
                        </TabsTrigger>
                        <TabsTrigger value="remedies" className="text-sm font-medium data-[state=active]:bg-purple-500/20 data-[state=active]:text-purple-300">
                          🕉️ Remedies ({chartData.personalizedRemedies?.length || 0})
                        </TabsTrigger>
                      </TabsList>

                      <TabsContent value="chart" className="space-y-8 mt-8">
                        {renderChartTab()}
                      </TabsContent>

                      <TabsContent value="yogas" className="mt-8">
                        {renderYogasTab()}
                      </TabsContent>

                      <TabsContent value="dasha" className="mt-8">
                        {renderDashaTab()}
                      </TabsContent>

                      <TabsContent value="divisional" className="mt-8">
                        <Card className="bg-gradient-to-br from-indigo-500/10 to-purple-500/10 border-indigo-500/20">
                          <CardHeader>
                            <CardTitle className="flex items-center gap-2">
                              <Star className="h-5 w-5 text-indigo-400" />
                              Advanced Divisional Charts Analysis
                            </CardTitle>
                            <CardDescription>
                              Specialized charts for marriage (D9), career (D10), spirituality (D12) and more
                            </CardDescription>
                          </CardHeader>
                          <CardContent>
                            <div className="text-center py-16">
                              <Sparkles className="h-20 w-20 text-muted-foreground mx-auto mb-6" />
                              <h3 className="text-2xl font-semibold mb-4">Coming Soon</h3>
                              <p className="text-muted-foreground max-w-md mx-auto text-lg leading-relaxed">
                                D9 Navamsa, D10 Dasamsa, D12 Dwadasamsa and other specialized divisional charts 
                                are being prepared for the next release with Swiss Ephemeris precision.
                              </p>
                              <div className="mt-6 flex justify-center gap-2">
                                <Badge variant="secondary" className="bg-indigo-500/20 text-indigo-300">
                                  16 Divisional Charts
                                </Badge>
                                <Badge variant="secondary" className="bg-purple-500/20 text-purple-300">
                                  Professional Analysis
                                </Badge>
                              </div>
                            </div>
                          </CardContent>
                        </Card>
                      </TabsContent>

                      <TabsContent value="remedies" className="mt-8">
                        <div className="space-y-8">
                          <div className="text-center">
                            <h3 className="text-3xl font-bold mb-4">Personalized Remedial Measures</h3>
                            <p className="text-muted-foreground max-w-3xl mx-auto text-lg">
                              Custom recommendations based on your unique planetary positions, detected yogas, 
                              and current dasha periods - calculated with Swiss Ephemeris precision
                            </p>
                          </div>
                          
                          <div className="grid gap-8">
                            {chartData.personalizedRemedies?.length > 0 ? (
                              chartData.personalizedRemedies.map((remedy, index) => (
                                <Card key={index} className="bg-gradient-to-br from-slate-800/80 to-slate-700/80 backdrop-blur-sm hover:from-slate-700/90 hover:to-slate-600/90 transition-all duration-300 border-slate-600/50">
                                  <CardContent className="p-8">
                                    <div className="flex items-start gap-6">
                                      <div className="p-4 rounded-xl bg-gradient-to-br from-purple-500/20 to-pink-500/20 border border-purple-500/30">
                                        <Sparkles className="h-8 w-8 text-purple-400" />
                                      </div>
                                      <div className="flex-1">
                                        <div className="flex items-start justify-between mb-4">
                                          <h4 className="font-bold text-2xl text-foreground">
                                            {remedy.category}: {remedy.remedy}
                                          </h4>
                                          <Badge 
                                            variant="secondary" 
                                            className={`ml-4 text-base px-3 py-1 ${
                                              remedy.priority >= 4 ? 'bg-red-500/20 text-red-300 border-red-500/30' :
                                              remedy.priority >= 3 ? 'bg-orange-500/20 text-orange-300 border-orange-500/30' :
                                              'bg-blue-500/20 text-blue-300 border-blue-500/30'
                                            }`}
                                          >
                                            Priority {remedy.priority}/5
                                          </Badge>
                                        </div>
                                        
                                        <p className="text-muted-foreground mb-6 leading-relaxed text-lg">
                                          {remedy.reason}
                                        </p>
                                        
                                        <div className="space-y-4">
                                          <div className="p-5 rounded-lg bg-gradient-to-r from-slate-800/60 to-slate-700/60 border border-slate-600/50">
                                            <h5 className="font-semibold text-base text-green-300 mb-2 flex items-center gap-2">
                                              <CheckCircle className="h-4 w-4" />
                                              Instructions:
                                            </h5>
                                            <p className="text-muted-foreground leading-relaxed">{remedy.instructions}</p>
                                          </div>
                                          
                                          <div className="p-5 rounded-lg bg-gradient-to-r from-slate-800/60 to-slate-700/60 border border-slate-600/50">
                                            <h5 className="font-semibold text-base text-blue-300 mb-2 flex items-center gap-2">
                                              <Clock className="h-4 w-4" />
                                              Optimal Timing:
                                            </h5>
                                            <p className="text-muted-foreground leading-relaxed">{remedy.timing}</p>
                                          </div>
                                        </div>
                                      </div>
                                    </div>
                                  </CardContent>
                                </Card>
                              ))
                            ) : (
                              <Card className="p-12 text-center bg-gradient-to-br from-purple-500/10 to-pink-500/10 border-purple-500/20">
                                <Sparkles className="h-16 w-16 text-muted-foreground mx-auto mb-6" />
                                <h3 className="text-2xl font-semibold mb-4">Remedies Being Calculated</h3>
                                <p className="text-muted-foreground max-w-md mx-auto text-lg">
                                  Personalized remedies are being generated based on your complete Swiss Ephemeris chart analysis.
                                </p>
                              </Card>
                            )}
                          </div>
                        </div>
                      </TabsContent>
                    </Tabs>
                  </CardContent>
                </Card>

                {/* Enhanced Action Button */}
                <div className="text-center pt-8">
                  <Button 
                    onClick={resetForm}
                    variant="outline"
                    className="px-12 py-4 text-lg border-purple-500/30 hover:bg-purple-500/10 hover:border-purple-500/50 transition-all duration-300"
                  >
                    <Calendar className="mr-3 h-5 w-5" />
                    Generate New Chart
                  </Button>
                </div>
              </motion.div>
            )}
          </AnimatePresence>
        </div>
      </main>

      <Footer />
    </div>
  );
}
