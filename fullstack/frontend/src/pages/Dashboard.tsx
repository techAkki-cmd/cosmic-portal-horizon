// src/pages/Dashboard.tsx
import React, { useState, useEffect, useMemo, useCallback } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { Button } from '@/components/ui/button';
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card';
import { Badge } from '@/components/ui/badge';
import { Progress } from '@/components/ui/progress';
import { Alert, AlertDescription } from '@/components/ui/alert';
import { Skeleton } from '@/components/ui/skeleton';
import { Tabs, TabsContent, TabsList, TabsTrigger } from '@/components/ui/tabs';
import { CosmicBackground } from '@/components/CosmicBackground';
import { CosmicWeatherCard } from '@/components/CosmicWeatherCard';
import { PlanetaryTicker } from '@/components/PlanetaryTicker';
import { Navigation } from '@/components/Navigation';
import { Footer } from '@/components/Footer';
import { motion, useScroll, useTransform, AnimatePresence, Variants } from 'framer-motion';
import { useAuth } from '@/contexts/AuthContext';
import { astrologyService } from '@/services/astrologyService';
import type { 
  PersonalizedMessage, 
  AstrologyUserStats as UserStats,
  LifeAreaInfluence,
  YogaAnalysisResponse,
  DashaAnalysisResponse,
  RemedialRecommendationsResponse,
  TransitResponse,
  BirthChartResponse
} from '@/types/astrology';
import { 
  Star, Moon, Calendar, Sparkles, TrendingUp, Heart, Briefcase, Shield, 
  ArrowRight, Plus, Zap, Eye, Clock, Award, BarChart3, Compass, Crown,
  RefreshCw, AlertCircle, CheckCircle, Info, Settings, User, Mail,
  Phone, MapPin, Sunrise, Users, Target, BookOpen, Lightbulb, Activity,
  Gem, Mic, Dumbbell, Home, Brain, TreePine, Scroll, ChevronRight,
  Calendar as CalendarIcon, TrendingDown, Timer, Waves, Sun, Globe
} from 'lucide-react';

// ================ ENHANCED INTERFACES AND TYPES ================

interface ComprehensiveDashboardState {
  personalizedMessage: PersonalizedMessage | null;
  lifeAreas: LifeAreaInfluence[];
  userStats: UserStats | null;
  yogaAnalysis: YogaAnalysisResponse | null;
  dashaAnalysis: DashaAnalysisResponse | null;
  remedialRecommendations: RemedialRecommendationsResponse | null;
  currentTransits: TransitResponse[];
  birthChart: BirthChartResponse | null;
  isLoading: boolean;
  error: string | null;
  lastUpdated: Date | null;
  retryCount: number;
  hasAdvancedFeatures: boolean;
}

interface EnhancedQuickAction {
  id: string;
  title: string;
  description: string;
  href: string;
  icon: React.ReactNode;
  color: string;
  bgColor: string;
  borderColor: string;
  isNew: boolean;
  isPremium?: boolean;
  category: 'chart' | 'analysis' | 'insights' | 'tools' | 'remedies';
  priority: number;
}

interface UserStatItem {
  id: string;
  label: string;
  value: string;
  icon: React.ReactNode;
  change: string;
  trend: 'up' | 'down' | 'neutral';
  color: string;
  description?: string;
}

// ================ ENHANCED CONSTANTS ================

const LIFE_AREA_ICONS: { [key: string]: React.ReactNode } = {
  Heart: <Heart className="h-6 w-6" />,
  Briefcase: <Briefcase className="h-6 w-6" />,
  Shield: <Shield className="h-6 w-6" />,
  Sparkles: <Sparkles className="h-6 w-6" />,
  Users: <Users className="h-6 w-6" />,
  Target: <Target className="h-6 w-6" />,
  BookOpen: <BookOpen className="h-6 w-6" />,
  Lightbulb: <Lightbulb className="h-6 w-6" />,
  Home: <Home className="h-6 w-6" />,
  Brain: <Brain className="h-6 w-6" />
};

const REMEDY_ICONS: { [key: string]: React.ReactNode } = {
  'Gemstone Remedies': <Gem className="h-4 w-4" />,
  'Mantra & Yantra Remedies': <Mic className="h-4 w-4" />,
  'Health & Wellness Remedies': <Shield className="h-4 w-4" />,
  'Career & Prosperity Remedies': <Briefcase className="h-4 w-4" />,
  'Relationship Harmony Remedies': <Heart className="h-4 w-4" />,
  'Lifestyle & Behavioral Remedies': <Activity className="h-4 w-4" />
};

const ENHANCED_QUICK_ACTIONS: EnhancedQuickAction[] = [
  {
    id: 'complete-analysis',
    title: "Complete Cosmic Analysis",
    description: "Comprehensive birth chart with yogas, dashas & remedies",
    href: "/complete-analysis",
    icon: <Compass className="h-5 w-5" />,
    color: "from-purple-500 to-indigo-500",
    bgColor: "bg-purple-500/10",
    borderColor: "border-purple-500/20",
    isNew: true,
    isPremium: true,
    category: 'analysis',
    priority: 1
  },
  {
    id: 'yoga-analysis',
    title: "Vedic Yoga Analysis",
    description: "Discover 200+ yogas in your chart",
    href: "/yoga-analysis",
    icon: <Sparkles className="h-5 w-5" />,
    color: "from-orange-500 to-red-500",
    bgColor: "bg-orange-500/10",
    borderColor: "border-orange-500/20",
    isNew: true,
    isPremium: true,
    category: 'analysis',
    priority: 2
  },
  {
    id: 'dasha-timeline',
    title: "Dasha Timeline",
    description: "Current & upcoming planetary periods",
    href: "/dasha-analysis",
    icon: <CalendarIcon className="h-5 w-5" />,
    color: "from-blue-500 to-cyan-500",
    bgColor: "bg-blue-500/10",
    borderColor: "border-blue-500/20",
    isNew: true,
    category: 'insights',
    priority: 3
  },
  {
    id: 'personalized-remedies',
    title: "Personalized Remedies",
    description: "12-category remedial recommendations",
    href: "/remedies",
    icon: <TreePine className="h-5 w-5" />,
    color: "from-emerald-500 to-teal-500",
    bgColor: "bg-emerald-500/10",
    borderColor: "border-emerald-500/20",
    isNew: true,
    isPremium: true,
    category: 'remedies',
    priority: 4
  },
  {
    id: 'current-transits',
    title: "Live Planetary Transits",
    description: "Real-time cosmic influences",
    href: "/transits",
    icon: <Globe className="h-5 w-5" />,
    color: "from-violet-500 to-purple-500",
    bgColor: "bg-violet-500/10",
    borderColor: "border-violet-500/20",
    isNew: false,
    category: 'insights',
    priority: 5
  },
  {
    id: 'birth-chart',
    title: "Enhanced Birth Chart",
    description: "Detailed natal chart with nakshatras",
    href: "/birth-chart",
    icon: <Moon className="h-5 w-5" />,
    color: "from-pink-500 to-rose-500",
    bgColor: "bg-pink-500/10",
    borderColor: "border-pink-500/20",
    isNew: false,
    category: 'chart',
    priority: 6
  }
];

const MAX_RETRY_ATTEMPTS = 3;
const REFRESH_INTERVAL = 5 * 60 * 1000; // 5 minutes

// ================ ANIMATION VARIANTS ================

const containerVariants: Variants = {
  hidden: { opacity: 0 },
  visible: {
    opacity: 1,
    transition: {
      staggerChildren: 0.08,
      delayChildren: 0.2
    }
  }
};

const itemVariants: Variants = {
  hidden: { opacity: 0, y: 20, scale: 0.95 },
  visible: {
    opacity: 1,
    y: 0,
    scale: 1,
    transition: {
      type: "spring",
      stiffness: 120,
      damping: 15
    }
  }
};

const cardHoverVariants: Variants = {
  rest: { scale: 1, y: 0 },
  hover: { 
    scale: 1.02, 
    y: -5,
    transition: {
      type: "spring",
      stiffness: 300,
      damping: 20
    }
  }
};

// ================ MAIN COMPONENT ================

export default function EnhancedDashboard() {
  const { user, isAuthenticated, refreshUser } = useAuth();
  const navigate = useNavigate();
  const { scrollY } = useScroll();
  
  const backgroundY = useTransform(scrollY, [0, 500], [0, 150]);
  const headerY = useTransform(scrollY, [0, 300], [0, 50]);

  // ================ ENHANCED STATE MANAGEMENT ================

  const [dashboardState, setDashboardState] = useState<ComprehensiveDashboardState>({
    personalizedMessage: null,
    lifeAreas: [],
    userStats: null,
    yogaAnalysis: null,
    dashaAnalysis: null,
    remedialRecommendations: null,
    currentTransits: [],
    birthChart: null,
    isLoading: true,
    error: null,
    lastUpdated: null,
    retryCount: 0,
    hasAdvancedFeatures: false
  });

  const [refreshing, setRefreshing] = useState(false);
  const [selectedTab, setSelectedTab] = useState('overview');
  const [selectedCategory, setSelectedCategory] = useState<string>('all');

  // ================ ENHANCED MEMOIZED VALUES ================

  const userDisplayName = useMemo(() => {
    if (!user) return 'Cosmic Explorer';
    return user.firstName || user.name || user.username || 'Cosmic Explorer';
  }, [user]);

  const currentDate = useMemo(() => {
    return new Date().toLocaleDateString('en-US', { 
      weekday: 'long', 
      month: 'long', 
      day: 'numeric',
      year: 'numeric'
    });
  }, []);

  const enhancedUserStats = useMemo((): UserStatItem[] => {
    if (!dashboardState.userStats) {
      return [
        { 
          id: 'charts',
          label: "Charts Created", 
          value: "0", 
          icon: <BarChart3 className="h-4 w-4" />, 
          change: "Get started!",
          trend: 'neutral',
          color: 'blue',
          description: 'Total birth charts generated'
        },
        { 
          id: 'yogas',
          label: "Yogas Detected", 
          value: dashboardState.yogaAnalysis?.totalYogas?.toString() || "0", 
          icon: <Sparkles className="h-4 w-4" />, 
          change: dashboardState.yogaAnalysis?.totalYogas ? `${dashboardState.yogaAnalysis.totalYogas} found` : "Generate chart first",
          trend: dashboardState.yogaAnalysis?.totalYogas ? 'up' : 'neutral',
          color: 'purple',
          description: 'Unique yoga combinations in chart'
        },
        { 
          id: 'remedies',
          label: "Active Remedies", 
          value: dashboardState.remedialRecommendations?.totalRemedies?.toString() || "0", 
          icon: <TreePine className="h-4 w-4" />, 
          change: dashboardState.remedialRecommendations?.totalRemedies ? "Personalized set" : "Analysis pending",
          trend: dashboardState.remedialRecommendations?.totalRemedies ? 'up' : 'neutral',
          color: 'emerald',
          description: 'Recommended remedial practices'
        },
        { 
          id: 'accuracy',
          label: "Accuracy Rate", 
          value: `95%`, 
          icon: <Award className="h-4 w-4" />, 
          change: "+3% this month",
          trend: 'up',
          color: 'yellow',
          description: 'Prediction accuracy percentage'
        }
      ];
    }

    const stats = dashboardState.userStats;
    return [
      { 
        id: 'charts',
        label: "Charts Created", 
        value: stats.chartsCreated?.toString() || "0", 
        icon: <BarChart3 className="h-4 w-4" />, 
        change: stats.chartsCreated > 0 ? "+2 this week" : "Get started!",
        trend: stats.chartsCreated > 0 ? 'up' : 'neutral',
        color: 'blue',
        description: 'Total birth charts generated'
      },
      { 
        id: 'yogas',
        label: "Yogas Detected", 
        value: dashboardState.yogaAnalysis?.totalYogas?.toString() || "0", 
        icon: <Sparkles className="h-4 w-4" />, 
        change: dashboardState.yogaAnalysis?.totalYogas ? `${dashboardState.yogaAnalysis.totalYogas} unique combinations` : "Generate analysis",
        trend: dashboardState.yogaAnalysis?.totalYogas ? 'up' : 'neutral',
        color: 'purple',
        description: 'Unique yoga combinations detected'
      },
      { 
        id: 'remedies',
        label: "Active Remedies", 
        value: dashboardState.remedialRecommendations?.totalRemedies?.toString() || "0", 
        icon: <TreePine className="h-4 w-4" />, 
        change: dashboardState.remedialRecommendations?.totalRemedies ? "Personalized guidance" : "Pending analysis",
        trend: dashboardState.remedialRecommendations?.totalRemedies ? 'up' : 'neutral',
        color: 'emerald',
        description: 'Recommended remedial practices'
      },
      { 
        id: 'accuracy',
        label: "Accuracy Rate", 
        value: `${stats.accuracyRate || 95}%`, 
        icon: <Award className="h-4 w-4" />, 
        change: "+3% this month",
        trend: 'up',
        color: 'yellow',
        description: 'Prediction accuracy percentage'
      }
    ];
  }, [dashboardState.userStats, dashboardState.yogaAnalysis, dashboardState.remedialRecommendations]);

  const filteredQuickActions = useMemo(() => {
    const sorted = ENHANCED_QUICK_ACTIONS.sort((a, b) => a.priority - b.priority);
    if (selectedCategory === 'all') return sorted;
    return sorted.filter(action => action.category === selectedCategory);
  }, [selectedCategory]);

  const profileCompletionPercentage = useMemo(() => {
    if (!user) return 0;
    
    let completed = 0;
    const totalFields = 8;
    
    if (user.firstName) completed++;
    if (user.lastName) completed++;
    if (user.email) completed++;
    if (user.birthDateTime) completed++;
    if (user.birthLocation) completed++;
    if (user.birthLatitude && user.birthLongitude) completed++;
    if (user.timezone) completed++;
    if (user.emailVerified) completed++;
    
    return Math.round((completed / totalFields) * 100);
  }, [user]);

  // ================ ENHANCED API CALLS ================

  const checkAdvancedFeatures = useCallback(async () => {
    try {
      const response = await fetch('/api/birth-chart/yoga-analysis', {
        method: 'GET',
        headers: {
          'Authorization': `Bearer ${localStorage.getItem('token')}`,
        },
      });
      return response.ok;
    } catch {
      return false;
    }
  }, []);

  const fetchComprehensiveDashboardData = useCallback(async (isRetry = false) => {
    if (!user) return;
    
    if (!isRetry) {
      setDashboardState(prev => ({ ...prev, isLoading: true, error: null }));
    }
    
    try {
      // Check if advanced features are available
      const hasAdvanced = await checkAdvancedFeatures();
      
      if (hasAdvanced && user.birthDateTime) {
        // Use new comprehensive analysis endpoint
        const [completeAnalysis, currentTransits] = await Promise.all([
          astrologyService.getCompleteAnalysis().catch(() => null),
          astrologyService.getCurrentTransits().catch(() => [])
        ]);

        if (completeAnalysis) {
          setDashboardState(prev => ({
            ...prev,
            personalizedMessage: completeAnalysis.personalizedMessage || createFallbackMessage(),
            lifeAreas: completeAnalysis.lifeAreaInfluences || createFallbackLifeAreas(),
            userStats: completeAnalysis.userStats || createFallbackStats(),
            yogaAnalysis: completeAnalysis.yogaAnalysis || null,
            dashaAnalysis: completeAnalysis.dashaAnalysis || null,
            remedialRecommendations: completeAnalysis.remedialRecommendations || null,
            birthChart: completeAnalysis.birthChart || null,
            currentTransits: currentTransits,
            isLoading: false,
            error: null,
            lastUpdated: new Date(),
            retryCount: 0,
            hasAdvancedFeatures: true
          }));
        } else {
          throw new Error('Failed to fetch comprehensive analysis');
        }
      } else {
        // Fallback to individual API calls
        const [messageData, lifeAreasData, statsData, transitsData] = await Promise.all([
          astrologyService.getPersonalizedMessage().catch(() => null),
          astrologyService.getLifeAreaInfluences().catch(() => []),
          astrologyService.getUserStats().catch(() => null),
          astrologyService.getCurrentTransits().catch(() => [])
        ]);
        
        setDashboardState(prev => ({
          ...prev,
          personalizedMessage: messageData || createFallbackMessage(),
          lifeAreas: lifeAreasData.length > 0 ? lifeAreasData : createFallbackLifeAreas(),
          userStats: statsData || createFallbackStats(),
          currentTransits: transitsData,
          isLoading: false,
          error: null,
          lastUpdated: new Date(),
          retryCount: 0,
          hasAdvancedFeatures: false
        }));
      }
      
    } catch (error) {
      console.error('Failed to fetch comprehensive dashboard data:', error);
      
      const errorMessage = error instanceof Error 
        ? error.message 
        : 'Failed to load personalized content';
      
      setDashboardState(prev => ({
        ...prev,
        personalizedMessage: prev.personalizedMessage || createFallbackMessage(),
        lifeAreas: prev.lifeAreas.length > 0 ? prev.lifeAreas : createFallbackLifeAreas(),
        userStats: prev.userStats || createFallbackStats(),
        isLoading: false,
        error: errorMessage,
        retryCount: prev.retryCount + 1,
        hasAdvancedFeatures: false
      }));
    }
  }, [user, checkAdvancedFeatures]);

  const handleRefresh = useCallback(async () => {
    setRefreshing(true);
    await fetchComprehensiveDashboardData(true);
    await refreshUser().catch(console.error);
    setTimeout(() => setRefreshing(false), 500);
  }, [fetchComprehensiveDashboardData, refreshUser]);

  // ================ EFFECTS ================

  useEffect(() => {
    if (!isAuthenticated) {
      navigate('/login');
      return;
    }
    
    fetchComprehensiveDashboardData();
  }, [isAuthenticated, navigate, fetchComprehensiveDashboardData]);

  useEffect(() => {
    const interval = setInterval(() => {
      if (!dashboardState.isLoading && !refreshing) {
        fetchComprehensiveDashboardData(true);
      }
    }, REFRESH_INTERVAL);

    return () => clearInterval(interval);
  }, [dashboardState.isLoading, refreshing, fetchComprehensiveDashboardData]);

  // ================ FALLBACK DATA CREATORS ================

  const createFallbackMessage = useCallback((): PersonalizedMessage => ({
    message: `🌅 Brahma Muhurta ${userDisplayName}! ${
      user?.birthDateTime 
        ? 'Your Vedic chart reveals Aquarius Lagna with Aries Moon in Ashwini Nakshatra. Today\'s cosmic influence brings divine opportunities for spiritual growth.' 
        : 'Complete your birth profile for personalized Vedic insights and accurate cosmic guidance.'
    }`,
    transitInfluence: user?.birthDateTime ? 
      "Current planetary energies support dharmic progress and spiritual evolution" : 
      "Complete birth data required for accurate Vedic calculations",
    recommendation: user?.birthDateTime ? 
      "Channel your warrior spirit through righteous action (Dharma)" : 
      "Add your birth information for personalized recommendations",
    intensity: user?.birthDateTime ? 4 : 2,
    dominantPlanet: "Chandra (Moon)",
    luckyColor: "Silver",
    bestTimeOfDay: "Brahma Muhurta (4-6 AM)",
    moonPhase: "Waxing Crescent"
  }), [userDisplayName, user?.birthDateTime]);

  const createFallbackLifeAreas = useCallback((): LifeAreaInfluence[] => [
    { 
      title: "Love & Relationships", 
      rating: user?.birthDateTime ? 4 : 2, 
      insight: user?.birthDateTime ? 
        "Venus and 7th house bring harmony to partnerships with current planetary support" : 
        "Complete your birth profile for personalized relationship insights", 
      icon: "Heart", 
      gradient: "from-pink-500 to-rose-500" 
    },
    { 
      title: "Career & Success", 
      rating: user?.birthDateTime ? 5 : 2, 
      insight: user?.birthDateTime ? 
        "10th house planets favor professional growth with current transits" : 
        "Add birth details for career guidance", 
      icon: "Briefcase", 
      gradient: "from-blue-500 to-indigo-500" 
    },
    { 
      title: "Health & Wellness", 
      rating: user?.birthDateTime ? 3 : 3, 
      insight: user?.birthDateTime ? 
        "6th house indicates focus on balanced lifestyle and wellness practices" : 
        "Birth chart analysis needed for health insights", 
      icon: "Shield", 
      gradient: "from-emerald-500 to-green-500" 
    },
    { 
      title: "Spiritual Growth", 
      rating: user?.birthDateTime ? 5 : 2, 
      insight: user?.birthDateTime ? 
        "9th and 12th houses strongly support spiritual practices and inner development" : 
        "Complete profile for spiritual guidance", 
      icon: "Sparkles", 
      gradient: "from-purple-500 to-violet-500" 
    },
    { 
      title: "Wealth & Prosperity", 
      rating: user?.birthDateTime ? 3 : 2, 
      insight: user?.birthDateTime ? 
        "2nd and 11th houses show steady financial progress with disciplined approach" : 
        "Birth details required for wealth analysis", 
      icon: "Target", 
      gradient: "from-green-500 to-emerald-500" 
    },
    { 
      title: "Family & Home", 
      rating: user?.birthDateTime ? 4 : 2, 
      insight: user?.birthDateTime ? 
        "4th house supports domestic happiness and family harmony" : 
        "Add birth information for family insights", 
      icon: "Home", 
      gradient: "from-orange-500 to-amber-500" 
    }
  ], [user?.birthDateTime]);

  const createFallbackStats = useCallback((): UserStats => ({
    chartsCreated: 0,
    accuracyRate: 95,
    cosmicEnergy: "Harmonious",
    streakDays: 1,
    totalReadings: 0,
    favoriteChartType: 'Natal',
    mostActiveTimeOfDay: 'Morning',
    averageSessionDuration: 0,
    totalPredictions: 0,
    correctPredictions: 0,
    lastChartGenerated: undefined
  }), []);

  // ================ RENDER METHODS ================

  const renderLoadingSkeleton = () => (
    <div className="min-h-screen bg-background text-foreground relative overflow-hidden">
      <CosmicBackground />
      <Navigation />
      <div className="pt-24 pb-8 px-4 sm:px-6 lg:px-8">
        <div className="max-w-7xl mx-auto space-y-8">
          <Skeleton className="h-64 w-full rounded-3xl" />
          <div className="grid grid-cols-1 xl:grid-cols-12 gap-8">
            <div className="xl:col-span-8 space-y-6">
              <Skeleton className="h-48 w-full rounded-xl" />
              <Skeleton className="h-64 w-full rounded-xl" />
            </div>
            <div className="xl:col-span-4 space-y-6">
              <Skeleton className="h-96 w-full rounded-xl" />
              <Skeleton className="h-48 w-full rounded-xl" />
            </div>
          </div>
        </div>
      </div>
    </div>
  );

  const renderEnhancedWelcomeSection = () => (
    <motion.section 
      style={{ y: headerY }}
      className="pt-24 pb-8 px-4 sm:px-6 lg:px-8"
    >
      <div className="max-w-7xl mx-auto">
        <motion.div
          initial={{ opacity: 0, y: 40 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ duration: 1, ease: "easeOut" }}
          className="relative"
        >
          {/* Enhanced Personalized Welcome with Advanced Features */}
          <div className="relative p-8 sm:p-12 rounded-3xl bg-gradient-to-br from-purple-900/80 via-blue-900/60 to-indigo-900/80 backdrop-blur-xl border border-white/10 overflow-hidden mb-8">
            {/* Animated Background Elements */}
            <div className="absolute inset-0">
              <motion.div 
                animate={{ 
                  scale: [1, 1.2, 1],
                  opacity: [0.1, 0.2, 0.1]
                }}
                transition={{ 
                  duration: 8, 
                  repeat: Infinity, 
                  ease: "easeInOut" 
                }}
                className="absolute top-4 right-4 w-32 h-32 bg-gradient-to-br from-white/5 to-transparent rounded-full blur-xl" 
              />
              <motion.div 
                animate={{ 
                  scale: [1, 1.1, 1],
                  opacity: [0.05, 0.15, 0.05]
                }}
                transition={{ 
                  duration: 6, 
                  repeat: Infinity, 
                  ease: "easeInOut",
                  delay: 2
                }}
                className="absolute bottom-4 left-4 w-24 h-24 bg-gradient-to-br from-purple-500/10 to-transparent rounded-full blur-lg" 
              />
            </div>
            
            <div className="relative z-10">
              <div className="flex flex-col lg:flex-row lg:items-start lg:justify-between gap-8">
                <div className="flex-1">
                  <div className="flex items-center gap-4 mb-6">
                    <motion.div
                      animate={{ rotate: 360 }}
                      transition={{ duration: 20, repeat: Infinity, ease: "linear" }}
                      className="p-3 rounded-full bg-gradient-to-br from-yellow-400 to-orange-500 shadow-lg shadow-yellow-500/25"
                    >
                      <Star className="h-8 w-8 text-white" />
                    </motion.div>
                    <div>
                      <h1 className="text-3xl sm:text-4xl lg:text-5xl font-playfair font-bold text-white mb-2">
                        Welcome back, {userDisplayName}!
                      </h1>
                      <div className="flex flex-wrap items-center gap-3">
                        <Badge variant="secondary" className="bg-white/10 text-white/80 border-white/20">
                          <Clock className="w-3 h-3 mr-1" />
                          {currentDate}
                        </Badge>
                        <Badge variant="outline" className="bg-orange-500/10 text-orange-200 border-orange-400/30">
                          <Info className="w-3 h-3 mr-1" />
                          Profile {profileCompletionPercentage}% Complete
                        </Badge>
                        {dashboardState.hasAdvancedFeatures && (
                          <Badge className="bg-gradient-to-r from-purple-400 to-pink-500 text-white border-0">
                            <Sparkles className="w-3 h-3 mr-1" />
                            Advanced Features
                          </Badge>
                        )}
                        {user?.subscriptionActive && (
                          <Badge className="bg-gradient-to-r from-yellow-400 to-yellow-500 text-yellow-900 border-0">
                            <Crown className="w-3 h-3 mr-1" />
                            Premium
                          </Badge>
                        )}
                      </div>
                    </div>
                  </div>
                  
                  {dashboardState.personalizedMessage && (
                    <motion.div 
                      initial={{ opacity: 0 }}
                      animate={{ opacity: 1 }}
                      transition={{ delay: 0.5 }}
                      className="space-y-4"
                    >
                      <p className="text-xl text-white/90 leading-relaxed font-light">
                        {dashboardState.personalizedMessage.message}
                      </p>
                      
                      {/* Enhanced Cosmic Intensity Display */}
                      <div className="flex flex-col sm:flex-row sm:items-center gap-4">
                        <div className="flex items-center gap-3">
                          <div className="flex">
                            {[...Array(5)].map((_, i) => (
                              <motion.div
                                key={i}
                                initial={{ scale: 0 }}
                                animate={{ scale: 1 }}
                                transition={{ delay: 0.7 + i * 0.1 }}
                              >
                                <Star
                                  className={`h-5 w-5 ${
                                    i < dashboardState.personalizedMessage!.intensity
                                      ? 'text-yellow-400 fill-current'
                                      : 'text-white/30'
                                  }`}
                                />
                              </motion.div>
                            ))}
                          </div>
                          <span className="text-white/70 text-sm font-medium">
                            Cosmic Intensity: {dashboardState.personalizedMessage.intensity}/5
                          </span>
                        </div>
                        
                        <div className="flex flex-wrap gap-2">
                          <div className="px-3 py-1.5 rounded-full bg-white/10 backdrop-blur-sm border border-white/20">
                            <span className="text-white/80 text-xs font-medium">
                              {dashboardState.personalizedMessage.transitInfluence}
                            </span>
                          </div>
                          {dashboardState.personalizedMessage.dominantPlanet && (
                            <div className="px-3 py-1.5 rounded-full bg-purple-500/20 backdrop-blur-sm border border-purple-400/30">
                              <span className="text-purple-200 text-xs font-medium">
                                ♃ {dashboardState.personalizedMessage.dominantPlanet}
                              </span>
                            </div>
                          )}
                        </div>
                      </div>

                      {/* Current Dasha Information */}
                      {dashboardState.dashaAnalysis && (
                        <motion.div 
                          initial={{ opacity: 0, y: 10 }}
                          animate={{ opacity: 1, y: 0 }}
                          transition={{ delay: 0.8 }}
                          className="mt-4 p-4 rounded-xl bg-blue-500/10 backdrop-blur-sm border border-blue-400/30"
                        >
                          <div className="flex items-start gap-3">
                            <CalendarIcon className="h-4 w-4 text-blue-400 mt-0.5 flex-shrink-0" />
                            <div>
                              <p className="text-blue-200 text-sm font-medium mb-1">Current Dasha Period</p>
                              <p className="text-blue-100 text-sm leading-relaxed">
                                {dashboardState.dashaAnalysis.currentMahadasha} Mahadasha → {dashboardState.dashaAnalysis.currentAntardasha} Antardasha
                              </p>
                              <p className="text-blue-200/70 text-xs mt-1">
                                {dashboardState.dashaAnalysis.mahadashaRemaining}
                              </p>
                            </div>
                          </div>
                        </motion.div>
                      )}
                      
                      {dashboardState.personalizedMessage.recommendation && (
                        <motion.div 
                          initial={{ opacity: 0, y: 10 }}
                          animate={{ opacity: 1, y: 0 }}
                          transition={{ delay: 1 }}
                          className="mt-4 p-4 rounded-xl bg-white/5 backdrop-blur-sm border border-white/10"
                        >
                          <div className="flex items-start gap-3">
                            <Lightbulb className="h-4 w-4 text-yellow-400 mt-0.5 flex-shrink-0" />
                            <div>
                              <p className="text-white/90 text-sm font-medium mb-1">Today's Guidance</p>
                              <p className="text-white/70 text-sm leading-relaxed">
                                {dashboardState.personalizedMessage.recommendation}
                              </p>
                            </div>
                          </div>
                        </motion.div>
                      )}
                    </motion.div>
                  )}
                </div>

                {/* Enhanced User Stats Panel with New Metrics */}
                <motion.div 
                  initial={{ opacity: 0, x: 20 }}
                  animate={{ opacity: 1, x: 0 }}
                  transition={{ delay: 0.8 }}
                  className="grid grid-cols-2 gap-3 sm:w-80"
                >
                  {enhancedUserStats.map((stat, index) => (
                    <motion.div
                      key={stat.id}
                      initial={{ opacity: 0, scale: 0.9 }}
                      animate={{ opacity: 1, scale: 1 }}
                      transition={{ delay: 1 + index * 0.1 }}
                      whileHover={{ scale: 1.05 }}
                      className="group p-4 rounded-xl bg-white/5 backdrop-blur-sm border border-white/10 hover:bg-white/10 transition-all duration-300 cursor-pointer"
                      title={stat.description}
                    >
                      <div className="flex items-center gap-2 mb-2">
                        <div className={`text-${stat.color}-400 group-hover:text-${stat.color}-300 transition-colors`}>
                          {stat.icon}
                        </div>
                        <span className="text-white/70 text-xs font-medium">{stat.label}</span>
                        {stat.trend === 'up' && (
                          <TrendingUp className="h-3 w-3 text-green-400" />
                        )}
                      </div>
                      <div className="text-white text-lg font-bold mb-1">{stat.value}</div>
                      <div className="text-white/50 text-xs">{stat.change}</div>
                    </motion.div>
                  ))}
                </motion.div>
              </div>
            </div>
          </div>

          {/* Profile Completion Alert */}
          {profileCompletionPercentage < 80 && (
            <motion.div
              initial={{ opacity: 0, y: 20 }}
              animate={{ opacity: 1, y: 0 }}
              transition={{ delay: 1.2 }}
            >
              <Alert className="mb-8 bg-gradient-to-r from-orange-500/10 to-red-500/10 border-orange-500/20">
                <AlertCircle className="h-4 w-4 text-orange-400" />
                <AlertDescription className="flex items-center justify-between">
                  <div>
                    <strong className="text-orange-300">Complete your profile ({profileCompletionPercentage}%)</strong>
                    <p className="text-sm text-orange-200/80 mt-1">
                      Add your birth information to unlock personalized Vedic insights, comprehensive yoga analysis, and accurate dasha predictions.
                    </p>
                  </div>
                  <Link to="/profile">
                    <Button variant="outline" size="sm" className="ml-4 border-orange-400/50 text-orange-300 hover:bg-orange-500/20">
                      Complete Profile
                    </Button>
                  </Link>
                </AlertDescription>
              </Alert>
            </motion.div>
          )}

          {/* Enhanced Planetary Ticker */}
          <motion.div
            initial={{ opacity: 0, y: 20 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ delay: 1 }}
          >
            <PlanetaryTicker />
          </motion.div>
        </motion.div>
      </div>
    </motion.section>
  );

  const renderEnhancedMainContent = () => (
    <section className="px-4 sm:px-6 lg:px-8 pb-16">
      <div className="max-w-7xl mx-auto">
        
        {/* Enhanced Header with Data Status */}
        <div className="flex items-center justify-between mb-8">
          <div>
            <h2 className="text-3xl font-playfair font-bold text-foreground mb-2">
              Your Comprehensive Cosmic Dashboard
            </h2>
            <p className="text-muted-foreground">
              {dashboardState.hasAdvancedFeatures 
                ? 'Complete Vedic analysis with advanced features' 
                : 'Enhanced with traditional Vedic insights'}
            </p>
          </div>
          <div className="flex items-center gap-4">
            {dashboardState.lastUpdated && (
              <Badge variant="outline" className="text-muted-foreground">
                <Clock className="w-3 h-3 mr-1" />
                Updated {dashboardState.lastUpdated.toLocaleTimeString('en-US', { 
                  hour: '2-digit', 
                  minute: '2-digit' 
                })}
              </Badge>
            )}
            <Button
              variant="outline"
              size="sm"
              onClick={handleRefresh}
              disabled={refreshing}
              className="flex items-center gap-2"
            >
              <RefreshCw className={`h-3 w-3 ${refreshing ? 'animate-spin' : ''}`} />
              Refresh
            </Button>
          </div>
        </div>

        {/* Error State Display */}
        {dashboardState.error && (
          <Alert variant="destructive" className="mb-6">
            <AlertCircle className="h-4 w-4" />
            <AlertDescription className="flex items-center justify-between">
              <span>{dashboardState.error}</span>
              {dashboardState.retryCount < MAX_RETRY_ATTEMPTS && (
                <Button
                  variant="outline"
                  size="sm"
                  onClick={() => fetchComprehensiveDashboardData(true)}
                  className="ml-4"
                >
                  <RefreshCw className="h-3 w-3 mr-1" />
                  Retry
                </Button>
              )}
            </AlertDescription>
          </Alert>
        )}

        {/* Enhanced Tabbed Interface */}
        <Tabs value={selectedTab} onValueChange={setSelectedTab} className="mb-12">
          <TabsList className="grid w-full grid-cols-4 mb-8">
            <TabsTrigger value="overview">Overview</TabsTrigger>
            <TabsTrigger value="yogas">Yogas</TabsTrigger>
            <TabsTrigger value="dashas">Dashas</TabsTrigger>
            <TabsTrigger value="remedies">Remedies</TabsTrigger>
          </TabsList>

          {/* Overview Tab */}
          <TabsContent value="overview">
            <motion.div
              variants={containerVariants}
              initial="hidden"
              animate="visible"
              className="grid grid-cols-1 xl:grid-cols-12 gap-8"
            >
              
              {/* Left Column - Enhanced Content */}
              <motion.div
                variants={itemVariants}
                className="xl:col-span-8 space-y-6"
              >
                <CosmicWeatherCard />
                
                {/* Current Transits Card */}
                {dashboardState.currentTransits.length > 0 && (
                  <Card className="bg-card/60 backdrop-blur-xl border-mystical-mid/20">
                    <CardHeader>
                      <CardTitle className="flex items-center gap-2">
                        <Globe className="h-5 w-5 text-celestial-mid" />
                        Current Planetary Transits
                      </CardTitle>
                      <CardDescription>
                        Real-time planetary positions and their influences
                      </CardDescription>
                    </CardHeader>
                    <CardContent>
                      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
                        {dashboardState.currentTransits.slice(0, 6).map((transit, index) => (
                          <motion.div
                            key={`${transit.planet}-${index}`}
                            initial={{ opacity: 0, y: 20 }}
                            animate={{ opacity: 1, y: 0 }}
                            transition={{ delay: index * 0.1 }}
                            className="p-4 rounded-xl bg-gradient-to-br from-blue-500/10 to-purple-500/10 border border-blue-500/20"
                          >
                            <div className="flex items-center gap-2 mb-2">
                              <Sun className="h-4 w-4 text-blue-500" />
                              <span className="font-semibold text-foreground">{transit.planet}</span>
                            </div>
                            <div className="text-sm text-muted-foreground space-y-1">
                              <p>{transit.position?.toFixed(2)}° in {transit.sign}</p>
                              <p>{transit.nakshatra} (Pada {transit.pada})</p>
                              {transit.influence && (
                                <p className="text-xs text-blue-600 dark:text-blue-400 italic">
                                  {transit.influence}
                                </p>
                              )}
                            </div>
                          </motion.div>
                        ))}
                      </div>
                    </CardContent>
                  </Card>
                )}

                {/* Enhanced Life Areas */}
                <Card className="bg-card/60 backdrop-blur-xl border-mystical-mid/20">
                  <CardHeader>
                    <CardTitle className="flex items-center gap-2">
                      <Compass className="h-5 w-5 text-celestial-mid" />
                      Life Area Influences
                    </CardTitle>
                    <CardDescription>
                      Current planetary impacts on different aspects of your life
                    </CardDescription>
                  </CardHeader>
                  <CardContent>
                    <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
                      {dashboardState.lifeAreas.map((area, index) => (
                        <motion.div
                          key={`${area.title}-${index}`}
                          initial={{ opacity: 0, y: 20 }}
                          animate={{ opacity: 1, y: 0 }}
                          transition={{ delay: index * 0.1 }}
                          whileHover={{ scale: 1.02 }}
                          className="p-4 rounded-xl bg-gradient-to-br from-white/5 to-white/10 border border-white/10 cursor-pointer group"
                        >
                          <div className="flex items-center gap-3 mb-3">
                            <div className={`p-2 rounded-lg bg-gradient-to-br ${area.gradient} text-white`}>
                              {LIFE_AREA_ICONS[area.icon] || <Sparkles className="h-4 w-4" />}
                            </div>
                            <h4 className="font-semibold text-foreground">{area.title}</h4>
                          </div>
                          
                          <div className="flex items-center gap-2 mb-2">
                            <div className="flex">
                              {[...Array(5)].map((_, i) => (
                                <Star
                                  key={i}
                                  className={`h-3 w-3 ${
                                    i < area.rating ? 'text-yellow-500 fill-current' : 'text-muted-foreground/30'
                                  }`}
                                />
                              ))}
                            </div>
                            <span className="text-sm text-muted-foreground">{area.rating}/5</span>
                          </div>
                          
                          <p className="text-xs text-muted-foreground leading-relaxed">
                            {area.insight}
                          </p>
                          
                          <Progress value={area.rating * 20} className="h-1 mt-3" />
                        </motion.div>
                      ))}
                    </div>
                  </CardContent>
                </Card>
              </motion.div>

              {/* Right Column - Enhanced Quick Actions */}
              <motion.div
                variants={itemVariants}
                className="xl:col-span-4 space-y-6"
              >
                {/* Enhanced Quick Actions */}
                <Card className="bg-card/60 backdrop-blur-xl border-mystical-mid/20">
                  <CardHeader>
                    <CardTitle className="flex items-center gap-2">
                      <Sparkles className="h-5 w-5 text-celestial-mid" />
                      Cosmic Tools
                    </CardTitle>
                    <CardDescription>
                      Explore your enhanced cosmic toolkit
                    </CardDescription>
                    
                    {/* Category Filter */}
                    <div className="flex flex-wrap gap-2 pt-2">
                      {['all', 'chart', 'analysis', 'insights', 'tools', 'remedies'].map((category) => (
                        <Button
                          key={category}
                          variant={selectedCategory === category ? "default" : "outline"}
                          size="sm"
                          onClick={() => setSelectedCategory(category)}
                          className="text-xs capitalize"
                        >
                          {category}
                        </Button>
                      ))}
                    </div>
                  </CardHeader>
                  <CardContent className="space-y-3">
                    <AnimatePresence mode="wait">
                      {filteredQuickActions.map((action, index) => (
                        <motion.div
                          key={action.id}
                          initial={{ opacity: 0, y: 20 }}
                          animate={{ opacity: 1, y: 0 }}
                          exit={{ opacity: 0, y: -20 }}
                          transition={{ delay: index * 0.05 }}
                          whileHover={{ scale: 1.02, y: -2 }}
                          className="group"
                        >
                          <Link to={action.href}>
                            <Button
                              variant="ghost"
                              className={`w-full justify-start p-4 h-auto hover:bg-gradient-to-r hover:${action.color} hover:text-white border ${action.borderColor} hover:border-transparent transition-all duration-300 group relative overflow-hidden`}
                            >
                              <div className="flex items-center gap-3 w-full relative z-10">
                                <div className={`p-2.5 rounded-xl ${action.bgColor} group-hover:bg-white/20 transition-colors`}>
                                  {action.icon}
                                </div>
                                <div className="text-left flex-1">
                                  <div className="flex items-center gap-2">
                                    <span className="font-medium text-foreground group-hover:text-white">
                                      {action.title}
                                    </span>
                                    {action.isNew && (
                                      <Badge variant="secondary" className="text-xs bg-gradient-to-r from-yellow-400 to-orange-500 text-white border-0">
                                        NEW
                                      </Badge>
                                    )}
                                    {action.isPremium && (
                                      <Badge variant="secondary" className="text-xs bg-gradient-to-r from-purple-400 to-pink-500 text-white border-0">
                                        <Crown className="w-2 h-2 mr-1" />
                                        PRO
                                      </Badge>
                                    )}
                                  </div>
                                  <div className="text-sm text-muted-foreground group-hover:text-white/80">
                                    {action.description}
                                  </div>
                                </div>
                                <ArrowRight className="h-4 w-4 text-muted-foreground group-hover:text-white transform group-hover:translate-x-1 transition-transform" />
                              </div>
                            </Button>
                          </Link>
                        </motion.div>
                      ))}
                    </AnimatePresence>
                  </CardContent>
                </Card>

                {/* Enhanced Cosmic Energy Meter */}
                <Card className="bg-card/60 backdrop-blur-xl border-mystical-mid/20">
                  <CardHeader className="pb-4">
                    <CardTitle className="flex items-center gap-2 text-base">
                      <Zap className="h-4 w-4 text-yellow-400" />
                      Cosmic Energy Level
                    </CardTitle>
                  </CardHeader>
                  <CardContent>
                    <div className="space-y-4">
                      <div className="flex justify-between text-sm">
                        <span className="text-muted-foreground">Current Level</span>
                        <span className="font-semibold text-foreground">
                          {dashboardState.userStats?.cosmicEnergy === "High" ? "92%" : 
                           dashboardState.userStats?.cosmicEnergy === "Medium" ? "65%" : "87%"}
                        </span>
                      </div>
                      <Progress 
                        value={
                          dashboardState.userStats?.cosmicEnergy === "High" ? 92 : 
                          dashboardState.userStats?.cosmicEnergy === "Medium" ? 65 : 87
                        } 
                        className="h-3" 
                      />
                      <div className="grid grid-cols-3 gap-2 text-xs">
                        <div className="text-center">
                          <div className="h-2 w-full bg-red-500/20 rounded-full mb-1">
                            <div className="h-full w-1/3 bg-red-500 rounded-full"></div>
                          </div>
                          <span className="text-muted-foreground">Low</span>
                        </div>
                        <div className="text-center">
                          <div className="h-2 w-full bg-yellow-500/20 rounded-full mb-1">
                            <div className="h-full w-2/3 bg-yellow-500 rounded-full"></div>
                          </div>
                          <span className="text-muted-foreground">Medium</span>
                        </div>
                        <div className="text-center">
                          <div className="h-2 w-full bg-green-500/20 rounded-full mb-1">
                            <div className="h-full w-full bg-green-500 rounded-full"></div>
                          </div>
                          <span className="text-muted-foreground">High</span>
                        </div>
                      </div>
                      <p className="text-xs text-muted-foreground">
                        {dashboardState.personalizedMessage?.moonPhase && 
                          `${dashboardState.personalizedMessage.moonPhase} energy influences your cosmic vibration`
                        }
                      </p>
                    </div>
                  </CardContent>
                </Card>
              </motion.div>
            </motion.div>
          </TabsContent>

          {/* Yogas Tab */}
          <TabsContent value="yogas">
            <motion.div
              initial={{ opacity: 0, y: 20 }}
              animate={{ opacity: 1, y: 0 }}
              transition={{ duration: 0.5 }}
            >
              {dashboardState.yogaAnalysis ? (
                <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
                  {/* Raja Yogas */}
                  <Card className="bg-card/60 backdrop-blur-xl border-mystical-mid/20">
                    <CardHeader>
                      <CardTitle className="flex items-center gap-2">
                        <Crown className="h-5 w-5 text-yellow-500" />
                        Raja Yogas ({dashboardState.yogaAnalysis.rajaYogas?.length || 0})
                      </CardTitle>
                      <CardDescription>Royal combinations for power and authority</CardDescription>
                    </CardHeader>
                    <CardContent>
                      {dashboardState.yogaAnalysis.rajaYogas?.length > 0 ? (
                        <div className="space-y-3">
                          {dashboardState.yogaAnalysis.rajaYogas.slice(0, 3).map((yoga: any, index) => (
                            <div key={index} className="p-3 rounded-lg bg-yellow-500/10 border border-yellow-500/20">
                              <h4 className="font-semibold text-yellow-600 dark:text-yellow-400 mb-1">
                                {yoga.remedy || yoga.yogaName || `Raja Yoga ${index + 1}`}
                              </h4>
                              <p className="text-sm text-muted-foreground">
                                {yoga.description || yoga.instructions || 'Powerful royal combination detected'}
                              </p>
                            </div>
                          ))}
                        </div>
                      ) : (
                        <p className="text-muted-foreground text-sm">No Raja Yogas detected. Complete your birth profile for detailed analysis.</p>
                      )}
                    </CardContent>
                  </Card>

                  {/* Dhana Yogas */}
                  <Card className="bg-card/60 backdrop-blur-xl border-mystical-mid/20">
                    <CardHeader>
                      <CardTitle className="flex items-center gap-2">
                        <Target className="h-5 w-5 text-green-500" />
                        Dhana Yogas ({dashboardState.yogaAnalysis.dhanaYogas?.length || 0})
                      </CardTitle>
                      <CardDescription>Wealth and prosperity combinations</CardDescription>
                    </CardHeader>
                    <CardContent>
                      {dashboardState.yogaAnalysis.dhanaYogas?.length > 0 ? (
                        <div className="space-y-3">
                          {dashboardState.yogaAnalysis.dhanaYogas.slice(0, 3).map((yoga: any, index) => (
                            <div key={index} className="p-3 rounded-lg bg-green-500/10 border border-green-500/20">
                              <h4 className="font-semibold text-green-600 dark:text-green-400 mb-1">
                                {yoga.remedy || yoga.yogaName || `Dhana Yoga ${index + 1}`}
                              </h4>
                              <p className="text-sm text-muted-foreground">
                                {yoga.description || yoga.instructions || 'Wealth combination detected'}
                              </p>
                            </div>
                          ))}
                        </div>
                      ) : (
                        <p className="text-muted-foreground text-sm">No Dhana Yogas detected. Generate your complete analysis for insights.</p>
                      )}
                    </CardContent>
                  </Card>

                  {/* Spiritual Yogas */}
                  <Card className="bg-card/60 backdrop-blur-xl border-mystical-mid/20">
                    <CardHeader>
                      <CardTitle className="flex items-center gap-2">
                        <Sparkles className="h-5 w-5 text-purple-500" />
                        Spiritual Yogas ({dashboardState.yogaAnalysis.spiritualYogas?.length || 0})
                      </CardTitle>
                      <CardDescription>Combinations for spiritual growth</CardDescription>
                    </CardHeader>
                    <CardContent>
                      {dashboardState.yogaAnalysis.spiritualYogas?.length > 0 ? (
                        <div className="space-y-3">
                          {dashboardState.yogaAnalysis.spiritualYogas.slice(0, 3).map((yoga: any, index) => (
                            <div key={index} className="p-3 rounded-lg bg-purple-500/10 border border-purple-500/20">
                              <h4 className="font-semibold text-purple-600 dark:text-purple-400 mb-1">
                                {yoga.remedy || yoga.yogaName || `Spiritual Yoga ${index + 1}`}
                              </h4>
                              <p className="text-sm text-muted-foreground">
                                {yoga.description || yoga.instructions || 'Spiritual combination detected'}
                              </p>
                            </div>
                          ))}
                        </div>
                      ) : (
                        <p className="text-muted-foreground text-sm">No Spiritual Yogas detected. Complete birth chart needed for analysis.</p>
                      )}
                    </CardContent>
                  </Card>

                  {/* Yoga Strength Overview */}
                  <Card className="bg-card/60 backdrop-blur-xl border-mystical-mid/20">
                    <CardHeader>
                      <CardTitle className="flex items-center gap-2">
                        <BarChart3 className="h-5 w-5 text-blue-500" />
                        Yoga Strength Analysis
                      </CardTitle>
                      <CardDescription>Overall yoga power in your chart</CardDescription>
                    </CardHeader>
                    <CardContent>
                      <div className="space-y-4">
                        <div className="flex justify-between items-center">
                          <span className="text-sm font-medium">Total Yogas Detected</span>
                          <Badge variant="outline" className="bg-blue-500/10 text-blue-600 dark:text-blue-400">
                            {dashboardState.yogaAnalysis.totalYogas || 0}
                          </Badge>
                        </div>
                        <div className="flex justify-between items-center">
                          <span className="text-sm font-medium">Yoga Strength Level</span>
                          <Badge variant="outline" className="bg-green-500/10 text-green-600 dark:text-green-400">
                            {dashboardState.yogaAnalysis.yogaStrength || 0}/5
                          </Badge>
                        </div>
                        <Progress value={(dashboardState.yogaAnalysis.yogaStrength || 0) * 20} className="h-2" />
                        <p className="text-xs text-muted-foreground">
                          Higher yoga strength indicates more powerful planetary combinations in your chart.
                        </p>
                      </div>
                    </CardContent>
                  </Card>
                </div>
              ) : (
                <Card className="bg-card/60 backdrop-blur-xl border-mystical-mid/20">
                  <CardContent className="text-center py-12">
                    <Sparkles className="h-16 w-16 text-muted-foreground mx-auto mb-4 opacity-50" />
                    <h3 className="text-xl font-semibold text-foreground mb-2">Yoga Analysis Pending</h3>
                    <p className="text-muted-foreground mb-6 leading-relaxed max-w-md mx-auto">
                      Complete your birth chart to unlock comprehensive yoga analysis with 200+ traditional combinations.
                    </p>
                    <Link to="/complete-analysis">
                      <Button className="bg-gradient-to-r from-purple-500 to-pink-500 hover:from-purple-600 hover:to-pink-600 text-white border-0 shadow-lg shadow-purple-500/25">
                        <Sparkles className="mr-2 h-4 w-4" />
                        Get Yoga Analysis
                      </Button>
                    </Link>
                  </CardContent>
                </Card>
              )}
            </motion.div>
          </TabsContent>

          {/* Dashas Tab */}
          <TabsContent value="dashas">
            <motion.div
              initial={{ opacity: 0, y: 20 }}
              animate={{ opacity: 1, y: 0 }}
              transition={{ duration: 0.5 }}
            >
              {dashboardState.dashaAnalysis ? (
                <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
                  {/* Current Dasha */}
                  <Card className="bg-card/60 backdrop-blur-xl border-mystical-mid/20">
                    <CardHeader>
                      <CardTitle className="flex items-center gap-2">
                        <CalendarIcon className="h-5 w-5 text-blue-500" />
                        Current Period
                      </CardTitle>
                    </CardHeader>
                    <CardContent>
                      <div className="space-y-4">
                        <div>
                          <label className="text-sm font-medium text-muted-foreground">Mahadasha</label>
                          <p className="text-lg font-semibold text-foreground">
                            {dashboardState.dashaAnalysis.currentMahadasha}
                          </p>
                        </div>
                        <div>
                          <label className="text-sm font-medium text-muted-foreground">Antardasha</label>
                          <p className="text-lg font-semibold text-foreground">
                            {dashboardState.dashaAnalysis.currentAntardasha}
                          </p>
                        </div>
                        {dashboardState.dashaAnalysis.currentPratyantardasha && (
                          <div>
                            <label className="text-sm font-medium text-muted-foreground">Pratyantardasha</label>
                            <p className="text-lg font-semibold text-foreground">
                              {dashboardState.dashaAnalysis.currentPratyantardasha}
                            </p>
                          </div>
                        )}
                        <div className="pt-2 border-t">
                          <label className="text-sm font-medium text-muted-foreground">Remaining</label>
                          <p className="text-sm text-foreground">
                            {dashboardState.dashaAnalysis.mahadashaRemaining}
                          </p>
                        </div>
                      </div>
                    </CardContent>
                  </Card>

                  {/* Dasha Interpretation */}
                  <Card className="lg:col-span-2 bg-card/60 backdrop-blur-xl border-mystical-mid/20">
                    <CardHeader>
                      <CardTitle className="flex items-center gap-2">
                        <BookOpen className="h-5 w-5 text-purple-500" />
                        Period Interpretation
                      </CardTitle>
                    </CardHeader>
                    <CardContent>
                      <div className="space-y-4">
                        <p className="text-foreground leading-relaxed">
                          {dashboardState.dashaAnalysis.dashaInterpretation || 
                           'Detailed interpretation based on current planetary periods and their influences on your life.'}
                        </p>
                        
                        {/* Favorable Periods */}
                        {dashboardState.dashaAnalysis.favorablePeriods && dashboardState.dashaAnalysis.favorablePeriods.length > 0 && (
                          <div>
                            <h4 className="font-semibold text-foreground mb-2">Favorable Times</h4>
                            <div className="flex flex-wrap gap-2">
                              {dashboardState.dashaAnalysis.favorablePeriods.map((period, index) => (
                                <Badge key={index} variant="secondary" className="bg-green-500/10 text-green-600 dark:text-green-400">
                                  {period}
                                </Badge>
                              ))}
                            </div>
                          </div>
                        )}

                        {/* Dasha Remedies */}
                        {dashboardState.dashaAnalysis.dashaRemedies && dashboardState.dashaAnalysis.dashaRemedies.length > 0 && (
                          <div>
                            <h4 className="font-semibold text-foreground mb-2">Recommended Practices</h4>
                            <ul className="space-y-1">
                              {dashboardState.dashaAnalysis.dashaRemedies.slice(0, 3).map((remedy, index) => (
                                <li key={index} className="text-sm text-muted-foreground flex items-start gap-2">
                                  <CheckCircle className="h-3 w-3 text-green-500 mt-0.5 flex-shrink-0" />
                                  {remedy}
                                </li>
                              ))}
                            </ul>
                          </div>
                        )}
                      </div>
                    </CardContent>
                  </Card>

                  {/* Upcoming Periods */}
                  {dashboardState.dashaAnalysis.upcomingPeriods && dashboardState.dashaAnalysis.upcomingPeriods.length > 0 && (
                    <Card className="lg:col-span-3 bg-card/60 backdrop-blur-xl border-mystical-mid/20">
                      <CardHeader>
                        <CardTitle className="flex items-center gap-2">
                          <Timer className="h-5 w-5 text-orange-500" />
                          Upcoming Periods
                        </CardTitle>
                        <CardDescription>Future dasha transitions and their themes</CardDescription>
                      </CardHeader>
                      <CardContent>
                        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
                          {dashboardState.dashaAnalysis.upcomingPeriods.slice(0, 6).map((period: any, index) => (
                            <div key={index} className="p-4 rounded-xl bg-gradient-to-br from-orange-500/10 to-red-500/10 border border-orange-500/20">
                              <div className="flex items-center gap-2 mb-2">
                                <CalendarIcon className="h-4 w-4 text-orange-500" />
                                <span className="font-semibold text-orange-600 dark:text-orange-400">
                                  {period.period || `Period ${index + 1}`}
                                </span>
                              </div>
                              <p className="text-sm text-muted-foreground mb-2">
                                {period.duration || 'Duration pending calculation'}
                              </p>
                              <p className="text-xs text-orange-600 dark:text-orange-400">
                                {period.theme || 'Planetary influence and growth opportunities'}
                              </p>
                            </div>
                          ))}
                        </div>
                      </CardContent>
                    </Card>
                  )}
                </div>
              ) : (
                <Card className="bg-card/60 backdrop-blur-xl border-mystical-mid/20">
                  <CardContent className="text-center py-12">
                    <CalendarIcon className="h-16 w-16 text-muted-foreground mx-auto mb-4 opacity-50" />
                    <h3 className="text-xl font-semibold text-foreground mb-2">Dasha Analysis Pending</h3>
                    <p className="text-muted-foreground mb-6 leading-relaxed max-w-md mx-auto">
                      Complete your birth information to unlock detailed Vimshottari dasha analysis with current and upcoming planetary periods.
                    </p>
                    <Link to="/complete-analysis">
                      <Button className="bg-gradient-to-r from-blue-500 to-purple-500 hover:from-blue-600 hover:to-purple-600 text-white border-0 shadow-lg shadow-blue-500/25">
                        <CalendarIcon className="mr-2 h-4 w-4" />
                        Get Dasha Analysis
                      </Button>
                    </Link>
                  </CardContent>
                </Card>
              )}
            </motion.div>
          </TabsContent>

          {/* Remedies Tab */}
          <TabsContent value="remedies">
            <motion.div
              initial={{ opacity: 0, y: 20 }}
              animate={{ opacity: 1, y: 0 }}
              transition={{ duration: 0.5 }}
            >
              {dashboardState.remedialRecommendations ? (
                <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
                  {/* Priority Remedies */}
                  {dashboardState.remedialRecommendations.priorityRemedies && dashboardState.remedialRecommendations.priorityRemedies.length > 0 && (
                    <Card className="lg:col-span-2 bg-gradient-to-br from-purple-500/10 to-pink-500/10 border-purple-500/20">
                      <CardHeader>
                        <CardTitle className="flex items-center gap-2">
                          <Star className="h-5 w-5 text-purple-500" />
                          Priority Remedies
                        </CardTitle>
                        <CardDescription>Most important remedial practices for your chart</CardDescription>
                      </CardHeader>
                      <CardContent>
                        <div className="space-y-4">
                          {dashboardState.remedialRecommendations.priorityRemedies.slice(0, 3).map((remedy: any, index) => (
                            <div key={index} className="p-4 rounded-xl bg-white/5 border border-white/10">
                              <div className="flex items-start gap-3">
                                <div className="p-2 rounded-lg bg-purple-500/20">
                                  <Star className="h-4 w-4 text-purple-400" />
                                </div>
                                <div className="flex-1">
                                  <h4 className="font-semibold text-foreground mb-1">
                                    {remedy.remedy || `Priority Remedy ${index + 1}`}
                                  </h4>
                                  <p className="text-sm text-muted-foreground mb-2">
                                    {remedy.instructions || remedy.description || 'Detailed instructions for this remedy'}
                                  </p>
                                  <div className="flex items-center gap-4 text-xs text-muted-foreground">
                                    <span>Priority: {remedy.priority || 'High'}</span>
                                    <span>Effectiveness: {remedy.effectiveness || 85}%</span>
                                  </div>
                                </div>
                              </div>
                            </div>
                          ))}
                        </div>
                      </CardContent>
                    </Card>
                  )}

                  {/* Gemstone Remedies */}
                  {dashboardState.remedialRecommendations.gemstoneRemedies && dashboardState.remedialRecommendations.gemstoneRemedies.length > 0 && (
                    <Card className="bg-card/60 backdrop-blur-xl border-mystical-mid/20">
                      <CardHeader>
                        <CardTitle className="flex items-center gap-2">
                          <Gem className="h-5 w-5 text-blue-500" />
                          Gemstone Remedies ({dashboardState.remedialRecommendations.gemstoneRemedies.length})
                        </CardTitle>
                      </CardHeader>
                      <CardContent>
                        <div className="space-y-3">
                          {dashboardState.remedialRecommendations.gemstoneRemedies.slice(0, 3).map((remedy: any, index) => (
                            <div key={index} className="p-3 rounded-lg bg-blue-500/10 border border-blue-500/20">
                              <h4 className="font-semibold text-blue-600 dark:text-blue-400 mb-1">
                                {remedy.remedy || `Gemstone ${index + 1}`}
                              </h4>
                              <p className="text-sm text-muted-foreground">
                                {remedy.instructions || 'Specific gemstone wearing instructions'}
                              </p>
                            </div>
                          ))}
                        </div>
                      </CardContent>
                    </Card>
                  )}

                  {/* Mantra Remedies */}
                  {dashboardState.remedialRecommendations.mantraRemedies && dashboardState.remedialRecommendations.mantraRemedies.length > 0 && (
                    <Card className="bg-card/60 backdrop-blur-xl border-mystical-mid/20">
                      <CardHeader>
                        <CardTitle className="flex items-center gap-2">
                          <Mic className="h-5 w-5 text-green-500" />
                          Mantra Remedies ({dashboardState.remedialRecommendations.mantraRemedies.length})
                        </CardTitle>
                      </CardHeader>
                      <CardContent>
                        <div className="space-y-3">
                          {dashboardState.remedialRecommendations.mantraRemedies.slice(0, 3).map((remedy: any, index) => (
                            <div key={index} className="p-3 rounded-lg bg-green-500/10 border border-green-500/20">
                              <h4 className="font-semibold text-green-600 dark:text-green-400 mb-1">
                                {remedy.remedy || `Mantra Practice ${index + 1}`}
                              </h4>
                              <p className="text-sm text-muted-foreground">
                                {remedy.instructions || 'Specific mantra chanting guidance'}
                              </p>
                            </div>
                          ))}
                        </div>
                      </CardContent>
                    </Card>
                  )}

                  {/* Health Remedies */}
                  {dashboardState.remedialRecommendations.healthRemedies && dashboardState.remedialRecommendations.healthRemedies.length > 0 && (
                    <Card className="bg-card/60 backdrop-blur-xl border-mystical-mid/20">
                      <CardHeader>
                        <CardTitle className="flex items-center gap-2">
                          <Shield className="h-5 w-5 text-emerald-500" />
                          Health Remedies ({dashboardState.remedialRecommendations.healthRemedies.length})
                        </CardTitle>
                      </CardHeader>
                      <CardContent>
                        <div className="space-y-3">
                          {dashboardState.remedialRecommendations.healthRemedies.slice(0, 3).map((remedy: any, index) => (
                            <div key={index} className="p-3 rounded-lg bg-emerald-500/10 border border-emerald-500/20">
                              <h4 className="font-semibold text-emerald-600 dark:text-emerald-400 mb-1">
                                {remedy.remedy || `Health Practice ${index + 1}`}
                              </h4>
                              <p className="text-sm text-muted-foreground">
                                {remedy.instructions || 'Specific health and wellness guidance'}
                              </p>
                            </div>
                          ))}
                        </div>
                      </CardContent>
                    </Card>
                  )}

                  {/* Lifestyle Remedies */}
                  {dashboardState.remedialRecommendations.lifestyleRemedies && dashboardState.remedialRecommendations.lifestyleRemedies.length > 0 && (
                    <Card className="bg-card/60 backdrop-blur-xl border-mystical-mid/20">
                      <CardHeader>
                        <CardTitle className="flex items-center gap-2">
                          <Activity className="h-5 w-5 text-orange-500" />
                          Lifestyle Remedies ({dashboardState.remedialRecommendations.lifestyleRemedies.length})
                        </CardTitle>
                      </CardHeader>
                      <CardContent>
                        <div className="space-y-3">
                          {dashboardState.remedialRecommendations.lifestyleRemedies.slice(0, 3).map((remedy: any, index) => (
                            <div key={index} className="p-3 rounded-lg bg-orange-500/10 border border-orange-500/20">
                              <h4 className="font-semibold text-orange-600 dark:text-orange-400 mb-1">
                                {remedy.remedy || `Lifestyle Change ${index + 1}`}
                              </h4>
                              <p className="text-sm text-muted-foreground">
                                {remedy.instructions || 'Specific lifestyle modification guidance'}
                              </p>
                            </div>
                          ))}
                        </div>
                      </CardContent>
                    </Card>
                  )}

                  {/* Overall Guidance */}
                  {dashboardState.remedialRecommendations.overallGuidance && (
                    <Card className="lg:col-span-2 bg-card/60 backdrop-blur-xl border-mystical-mid/20">
                      <CardHeader>
                        <CardTitle className="flex items-center gap-2">
                          <Lightbulb className="h-5 w-5 text-yellow-500" />
                          Overall Remedial Guidance
                        </CardTitle>
                      </CardHeader>
                      <CardContent>
                        <p className="text-foreground leading-relaxed">
                          {dashboardState.remedialRecommendations.overallGuidance}
                        </p>
                      </CardContent>
                    </Card>
                  )}
                </div>
              ) : (
                <Card className="bg-card/60 backdrop-blur-xl border-mystical-mid/20">
                  <CardContent className="text-center py-12">
                    <TreePine className="h-16 w-16 text-muted-foreground mx-auto mb-4 opacity-50" />
                    <h3 className="text-xl font-semibold text-foreground mb-2">Remedial Analysis Pending</h3>
                    <p className="text-muted-foreground mb-6 leading-relaxed max-w-md mx-auto">
                      Complete your birth chart to unlock personalized remedial recommendations across 12 categories including gemstones, mantras, and lifestyle guidance.
                    </p>
                    <Link to="/complete-analysis">
                      <Button className="bg-gradient-to-r from-emerald-500 to-teal-500 hover:from-emerald-600 hover:to-teal-600 text-white border-0 shadow-lg shadow-emerald-500/25">
                        <TreePine className="mr-2 h-4 w-4" />
                        Get Remedial Guidance
                      </Button>
                    </Link>
                  </CardContent>
                </Card>
              )}
            </motion.div>
          </TabsContent>
        </Tabs>
      </div>
    </section>
  );

  // ================ MAIN RENDER ================

  if (dashboardState.isLoading && !dashboardState.personalizedMessage) {
    return renderLoadingSkeleton();
  }

  return (
    <div className="min-h-screen bg-background text-foreground relative overflow-hidden">
      <CosmicBackground />
      <Navigation />
      
      <main className="relative z-10">
        {renderEnhancedWelcomeSection()}
        {renderEnhancedMainContent()}
      </main>

      <Footer />
    </div>
  );
}
