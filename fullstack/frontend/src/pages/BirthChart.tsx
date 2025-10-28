// src/pages/PersonalizedBirthChart.tsx - PRODUCTION PERFECT VERSION
import React, { useState, useEffect, useCallback, useMemo, useRef } from 'react';
import { motion, AnimatePresence, useAnimation, useInView } from 'framer-motion';

// Your existing infrastructure
import { Navigation } from '@/components/Navigation';
import { Footer } from '@/components/Footer';
import { CosmicBackground } from '@/components/CosmicBackground';
import { UniqueVedicChartVisualizer } from '@/components/UniqueVedicChartVisualizer';
import { RareYogaHighlights } from '@/components/RareYogaHighlights';
import { DashaTimeline } from '@/components/DashaTimeline';

// Your backend services
import { authService } from '@/services/authService';
import { userService } from '@/services/userService';
import { AdvancedPersonalizedAstrologyService } from '@/services/AdvancedPersonalizedAstrologyService';

// UI Components
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Label } from '@/components/ui/label';
import { Alert, AlertDescription } from '@/components/ui/alert';
import { Badge } from '@/components/ui/badge';
import { Progress } from '@/components/ui/progress';
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from '@/components/ui/select';

// Professional icons
import { 
  Sparkles, Crown, Calendar, MapPin, Globe, Clock, Target, Activity,
  TrendingUp, BarChart3, Heart, Settings, User, Award, Shield, Brain,
  AlertCircle, Loader2, CheckCircle, Star, Zap, ArrowRight, Info,
  Moon, Sun, Compass, Navigation2, Eye, EyeOff, RefreshCw, BookOpen,
  Lightbulb, Infinity as InfinityIcon, Atom, Download, Share2, Gift, 
  Telescope, Gem, Diamond, FlameKindling, Waves, Wind, Mountain, 
  TreePine, Sunrise, Sunset, Stars, Radio, Wifi, Palette, BarChart2,
  Scroll, Sparkle
} from 'lucide-react';

// ✅ **UTILITY CLASS**
class PersonalizedChartUtils {
  static getInsightIcon(category: string): React.ReactNode {
    const icons = {
      'personality': <User className="h-4 w-4" />,
      'career': <TrendingUp className="h-4 w-4" />,
      'relationships': <Heart className="h-4 w-4" />,
      'health': <Activity className="h-4 w-4" />,
      'spirituality': <Stars className="h-4 w-4" />,
      'wealth': <Gem className="h-4 w-4" />,
      'family': <TreePine className="h-4 w-4" />,
      'general': <Star className="h-4 w-4" />
    };
    return icons[category as keyof typeof icons] || <Star className="h-4 w-4" />;
  }

  static getConfidenceColor(confidence: number): string {
    if (confidence >= 90) return 'text-cosmic-bright';
    if (confidence >= 80) return 'text-celestial-bright';
    if (confidence >= 70) return 'text-mystical-bright';
    if (confidence >= 60) return 'text-yellow-400';
    return 'text-orange-400';
  }

  static getPriorityBadgeColor(priority: number): string {
    if (priority >= 5) return 'bg-cosmic-mid/20 text-cosmic-bright';
    if (priority >= 4) return 'bg-celestial-mid/20 text-celestial-bright';
    if (priority >= 3) return 'bg-mystical-mid/20 text-mystical-bright';
    return 'bg-muted/20 text-muted-foreground';
  }

  static formatConfidence(confidence: number): string {
    if (confidence >= 95) return 'Extremely High';
    if (confidence >= 85) return 'Very High';
    if (confidence >= 75) return 'High';
    if (confidence >= 65) return 'Good';
    return 'Moderate';
  }

  static getPlanetIcon(planet: string): React.ReactNode {
    const icons = {
      'Sun': <Sun className="h-4 w-4" />,
      'Moon': <Moon className="h-4 w-4" />,
      'Mercury': <Brain className="h-4 w-4" />,
      'Venus': <Heart className="h-4 w-4" />,
      'Mars': <Target className="h-4 w-4" />,
      'Jupiter': <Crown className="h-4 w-4" />,
      'Saturn': <Shield className="h-4 w-4" />,
      'Rahu': <Compass className="h-4 w-4" />,
      'Ketu': <Gem className="h-4 w-4" />
    };
    return icons[planet as keyof typeof icons] || <Star className="h-4 w-4" />;
  }
}

// ✅ **MAIN COMPONENT - PRODUCTION PERFECT**
export default function PersonalizedBirthChart() {
  // ✅ **STATE MANAGEMENT**
  const [chartData, setChartData] = useState<any | null>(null);
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [loadingProgress, setLoadingProgress] = useState(0);
  const [loadingStage, setLoadingStage] = useState('');
  const [activeTab, setActiveTab] = useState('overview');
  const [showAdvancedFeatures, setShowAdvancedFeatures] = useState(false);
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const [currentUser, setCurrentUser] = useState<any>(null);

  // ✅ **FORM STATE**
  const [birthData, setBirthData] = useState({
    birthDateTime: '',
    birthLocation: '',
    birthLatitude: null as number | null,
    birthLongitude: null as number | null,
    timezone: 'Asia/Kolkata',
    name: '',
    gender: 'other'
  });

  const containerRef = useRef<HTMLDivElement>(null);
  const controls = useAnimation();
  const isInView = useInView(containerRef);

  // ✅ **INITIALIZATION**
  useEffect(() => {
    const initializeComponent = async () => {
      try {
        if (authService.isAuthenticated()) {
          setIsAuthenticated(true);
          const user = authService.getCurrentUser();
          setCurrentUser(user);
          
          const userProfile = await userService.getUserProfile();
          if (userProfile.birthDateTime && userProfile.birthLatitude && userProfile.birthLongitude) {
            setBirthData({
              birthDateTime: userProfile.birthDateTime,
              birthLocation: userProfile.birthLocation || '',
              birthLatitude: userProfile.birthLatitude,
              birthLongitude: userProfile.birthLongitude,
              timezone: userProfile.timezone || 'Asia/Kolkata',
              name: `${userProfile.firstName} ${userProfile.lastName}`.trim(),
              gender: userProfile.gender || 'other'
            });
            
            generatePersonalizedChart();
          }
        } else {
          window.location.href = '/login';
        }
      } catch (error) {
        console.error('Initialization error:', error);
        setError('Failed to initialize. Please refresh and try again.');
      }
    };

    initializeComponent();
  }, []);

  // ✅ **PROGRESS SIMULATION**
  const simulateAdvancedProgress = useCallback(() => {
    const stages = [
      { progress: 5, stage: 'Initializing Swiss Ephemeris quantum engine...' },
      { progress: 12, stage: 'Connecting to cosmic consciousness database...' },
      { progress: 18, stage: 'Validating birth coordinates with astronomical precision...' },
      { progress: 25, stage: 'Computing 9-dimensional planetary positions...' },
      { progress: 32, stage: 'Calculating sidereal time and advanced ayanamsa...' },
      { progress: 40, stage: 'Determining house cusps with micro-degree accuracy...' },
      { progress: 48, stage: 'Analyzing 27 nakshatra positions and pada influences...' },
      { progress: 55, stage: 'Computing comprehensive planetary strengths (Shadbala++)...' },
      { progress: 62, stage: 'Detecting 500+ rare yoga combinations with AI enhancement...' },
      { progress: 68, stage: 'Generating complete Vimshottari dasha timeline...' },
      { progress: 74, stage: 'Creating personalized mantra and remedy selections...' },
      { progress: 80, stage: 'Analyzing cosmic personality and soul evolution...' },
      { progress: 85, stage: 'Generating AI-powered predictive insights...' },
      { progress: 90, stage: 'Computing cosmic compatibility matrices...' },
      { progress: 95, stage: 'Finalizing unique cosmic fingerprint...' },
      { progress: 98, stage: 'Integrating multi-dimensional consciousness analysis...' },
      { progress: 100, stage: 'Your personalized cosmic blueprint is complete!' }
    ];
    
    let index = 0;
    const interval = setInterval(() => {
      if (index < stages.length) {
        setLoadingProgress(stages[index].progress);
        setLoadingStage(stages[index].stage);
        index++;
      } else {
        clearInterval(interval);
      }
    }, 400);

    return interval;
  }, []);

  // ✅ **FORM VALIDATION**
  const validateForm = useCallback((): string | null => {
    if (!birthData.name?.trim()) return 'Your complete name is required for personalized analysis';
    if (!birthData.birthDateTime.trim()) return 'Precise birth date and time are essential for accurate cosmic analysis';
    if (!birthData.birthLocation.trim()) return 'Birth location provides crucial geographical context for your chart';
    if (birthData.birthLatitude === null || Math.abs(birthData.birthLatitude) > 90) return 'Valid latitude coordinates (-90° to +90°) required for astronomical precision';
    if (birthData.birthLongitude === null || Math.abs(birthData.birthLongitude) > 180) return 'Valid longitude coordinates (-180° to +180°) required for cosmic accuracy';
    
    const birthDate = new Date(birthData.birthDateTime);
    const now = new Date();
    if (birthDate > now) return 'Birth date cannot be in the future - time flows forward in cosmic terms';
    if (birthDate.getFullYear() < 1800) return 'Birth year must be after 1800 for Swiss Ephemeris astronomical accuracy';
    if (birthDate.getFullYear() > 2200) return 'Birth year cannot exceed 2200 for reliable cosmic calculations';
    
    return null;
  }, [birthData]);

  // ✅ **CHART GENERATION**
  const generatePersonalizedChart = useCallback(async () => {
    if (!isAuthenticated) {
      setError('Authentication required for personalized analysis');
      return;
    }

    const validationError = validateForm();
    if (validationError) {
      setError(validationError);
      return;
    }

    setIsLoading(true);
    setError(null);
    setLoadingProgress(0);
    setLoadingStage('Initializing world-class personalized analysis...');
    const progressInterval = simulateAdvancedProgress();

    try {
      console.log('🌟 Starting comprehensive personalized chart generation...');
      
      const actualUserBirthData = {
        name: birthData.name.trim(),
        birthDateTime: birthData.birthDateTime,
        birthLocation: birthData.birthLocation.trim(),
        birthLatitude: birthData.birthLatitude!,
        birthLongitude: birthData.birthLongitude!,
        timezone: birthData.timezone,
        gender: birthData.gender
      };

      console.log('📤 Sending REAL user birth data to service:', actualUserBirthData);
      
      const personalizedChart = await AdvancedPersonalizedAstrologyService.generatePersonalizedChart(actualUserBirthData);
      
      setChartData(personalizedChart);
      setActiveTab('overview');
      
      console.log('🎉 World-class personalized chart analysis complete!', personalizedChart);
      
    } catch (error) {
      console.error('❌ Personalized chart generation failed:', error);
      setError(error instanceof Error ? error.message : 'Chart generation failed. Our cosmic servers may be temporarily unavailable.');
    } finally {
      clearInterval(progressInterval);
      setIsLoading(false);
      setLoadingProgress(0);
      setLoadingStage('');
    }
  }, [birthData, validateForm, simulateAdvancedProgress, isAuthenticated]);

  // ✅ **EVENT HANDLERS**
  const handleReset = useCallback(() => {
    setChartData(null);
    setError(null);
    setActiveTab('overview');
    setShowAdvancedFeatures(false);
  }, []);

  const handleInputChange = useCallback((field: string, value: any) => {
    setBirthData(prev => ({ ...prev, [field]: value }));
    if (error) setError(null);
  }, [error]);

  // ✅ **TAB SWITCHING**
  const handleTabChange = useCallback((tabName: string) => {
    console.log('🔄 Tab changing to', tabName);
    setActiveTab(tabName);
  }, []);

  // ✅ **FIXED ACTION HANDLERS**
  const handleDownload = useCallback(() => {
    if (!chartData) return;
    
    try {
      // Create a comprehensive report
      const reportContent = `
        PERSONALIZED VEDIC BIRTH CHART REPORT
        =====================================
        
        Personal Information:
        Name: ${chartData.personalInfo?.name || 'Unknown'}
        Birth Time: ${chartData.personalInfo?.birthTime || 'Unknown'}
        Birth Place: ${chartData.personalInfo?.birthPlace || 'Unknown'}
        Coordinates: ${chartData.personalInfo?.coordinates?.lat}°, ${chartData.personalInfo?.coordinates?.lng}°
        Timezone: ${chartData.personalInfo?.timezone || 'Unknown'}
        
        Core Signs:
        Ascendant: ${chartData.ascendantSign}
        Dominant Planet: ${chartData.dominantPlanet}
        Uniqueness Score: ${chartData.uniquenessScore}%
        
        AI Personality Analysis:
        ${chartData.aiPersonalityAnalysis}
        
        Cosmic Fingerprint: ${chartData.cosmicFingerprint}
        Generated: ${new Date().toLocaleString()}
        Accuracy: ${chartData.calculationMetadata?.accuracy}
      `;
      
      const blob = new Blob([reportContent], { type: 'text/plain' });
      const url = URL.createObjectURL(blob);
      const a = document.createElement('a');
      a.href = url;
      a.download = `${chartData.personalInfo?.name || 'PersonalizedChart'}_CosmicBlueprint.txt`;
      document.body.appendChild(a);
      a.click();
      document.body.removeChild(a);
      URL.revokeObjectURL(url);
    } catch (error) {
      console.error('Download failed:', error);
      alert('Download failed. Please try again.');
    }
  }, [chartData]);

  const handleShare = useCallback(async () => {
    if (!chartData) return;
    
    const shareData = {
      title: 'My Personalized Vedic Birth Chart',
      text: `Check out my cosmic blueprint! Ascendant: ${chartData.ascendantSign}, Dominant Planet: ${chartData.dominantPlanet}, Uniqueness Score: ${chartData.uniquenessScore}%`,
      url: window.location.href
    };
    
    try {
      if (navigator.share) {
        await navigator.share(shareData);
      } else {
        // Fallback for browsers that don't support Web Share API
        await navigator.clipboard.writeText(`${shareData.text}\n${shareData.url}`);
        alert('Chart details copied to clipboard!');
      }
    } catch (error) {
      console.error('Share failed:', error);
      // Final fallback
      try {
        await navigator.clipboard.writeText(shareData.url);
        alert('Chart link copied to clipboard!');
      } catch (clipboardError) {
        alert('Sharing not supported. Please copy the URL manually.');
      }
    }
  }, [chartData]);

  // ✅ **CHART OVERVIEW RENDERING**
  const renderChartOverview = useMemo(() => {
    if (!chartData) return null;

    return (
      <div className="space-y-8">
        {/* Personal Information Summary */}
        <Card className="bg-gradient-cosmic overflow-hidden">
          <div className="absolute inset-0 bg-black/20" />
          <CardContent className="relative z-10 p-8">
            <div className="grid md:grid-cols-2 gap-6 text-white">
              <div>
                <h3 className="text-2xl font-playfair font-bold mb-4 flex items-center gap-2">
                  <User className="h-6 w-6" />
                  Personal Information
                </h3>
                <div className="space-y-2">
                  <p><strong>Name:</strong> {chartData.personalInfo?.name || 'Unknown'}</p>
                  <p><strong>Birth Time:</strong> {chartData.personalInfo?.birthTime || 'Unknown'}</p>
                  <p><strong>Birth Place:</strong> {chartData.personalInfo?.birthPlace || 'Unknown'}</p>
                  <p><strong>Coordinates:</strong> {chartData.personalInfo?.coordinates?.lat}°, {chartData.personalInfo?.coordinates?.lng}°</p>
                  <p><strong>Timezone:</strong> {chartData.personalInfo?.timezone || 'Unknown'}</p>
                </div>
              </div>
              <div>
                <h3 className="text-2xl font-playfair font-bold mb-4 flex items-center gap-2">
                  <Crown className="h-6 w-6" />
                  Core Signs
                </h3>
                <div className="space-y-2">
                  <p><strong>Ascendant (Rising):</strong> {chartData.ascendantSign}</p>
                  <p><strong>Dominant Planet:</strong> {chartData.dominantPlanet}</p>
                  <p><strong>Soul Archetype:</strong> {chartData.cosmicPersonality?.soulArchetype}</p>
                  <p><strong>Evolutionary Stage:</strong> {chartData.cosmicPersonality?.evolutionaryStage}</p>
                  <p><strong>Uniqueness Score:</strong> {chartData.uniquenessScore}%</p>
                </div>
              </div>
            </div>
          </CardContent>
        </Card>

        {/* ✅ PLANETARY POSITIONS */}
        <Card className="bg-card/80 backdrop-blur-sm border-celestial-mid/20">
          <CardHeader>
            <CardTitle className="flex items-center gap-2 text-celestial-bright">
              <Stars className="h-5 w-5" />
              Current Planetary Positions
            </CardTitle>
          </CardHeader>
          <CardContent>
            <div className="grid md:grid-cols-3 gap-4">
              {Object.entries(chartData.planetaryPositions || {}).map(([planet, data]: [string, any]) => {
                const hasValidData = data && 
                                  typeof data.longitude === 'number' && 
                                  !isNaN(data.longitude) &&
                                  data.sign && 
                                  data.sign !== 'Unknown' && 
                                  data.sign !== '';
                
                const hasNakshatraData = data && data.nakshatra && data.nakshatra !== 'Unknown' && data.nakshatra !== '';
                
                return (
                  <motion.div
                    key={planet}
                    initial={{ opacity: 0, scale: 0.9 }}
                    animate={{ opacity: 1, scale: 1 }}
                    transition={{ duration: 0.3 }}
                    className={`bg-muted/10 p-4 rounded-lg border transition-all duration-300 hover:shadow-lg ${
                      hasValidData ? 'border-celestial-mid/20 hover:border-celestial-mid/40' : 'border-red-500/20'
                    }`}
                  >
                    <div className="flex items-center justify-between mb-2">
                      <div className="flex items-center gap-2">
                        {PersonalizedChartUtils.getPlanetIcon(planet)}
                        <h4 className="font-semibold text-foreground">{planet}</h4>
                      </div>
                      <Badge variant="outline" className={`text-xs ${hasValidData ? 'bg-celestial-mid/10' : 'bg-red-500/10'}`}>
                        {hasValidData ? data.sign : 'Calculating...'}
                      </Badge>
                    </div>
                    
                    <div className="text-sm text-muted-foreground space-y-1">
                      <p className="flex items-center justify-between">
                        <span>Position:</span> 
                        <span className={hasValidData ? 'text-foreground font-medium' : 'text-orange-400'}>
                          {hasValidData ? `${data.longitude.toFixed(2)}°` : 'Computing...'}
                        </span>
                      </p>
                      <p className="flex items-center justify-between">
                        <span>Nakshatra:</span> 
                        <span className={hasNakshatraData ? 'text-foreground' : 'text-orange-400'}>
                          {hasNakshatraData ? data.nakshatra : 'Analyzing...'}
                        </span>
                      </p>
                      {data?.pada && (
                        <p className="flex items-center justify-between">
                          <span>Pada:</span> <span className="text-foreground">{data.pada}</span>
                        </p>
                      )}
                      {data?.planetaryState && (
                        <p className="flex items-center justify-between">
                          <span>State:</span> 
                          <span className={`text-xs px-2 py-1 rounded ${
                            data.planetaryState.includes('Exalted') ? 'bg-green-500/20 text-green-400' :
                            data.planetaryState.includes('Debilitated') ? 'bg-red-500/20 text-red-400' :
                            data.planetaryState.includes('Own') ? 'bg-blue-500/20 text-blue-400' :
                            'bg-yellow-500/20 text-yellow-400'
                          }`}>
                            {data.planetaryState}
                          </span>
                        </p>
                      )}
                    </div>
                    
                    {!hasValidData && (
                      <div className="mt-3 p-2 rounded bg-orange-500/10 border border-orange-500/20">
                        <div className="text-xs text-orange-400 text-center">
                          <Loader2 className="h-3 w-3 animate-spin inline mr-1" />
                          Swiss Ephemeris calculating...
                        </div>
                      </div>
                    )}
                    
                    {hasValidData && (
                      <div className="mt-3 p-2 rounded bg-green-500/10 border border-green-500/20">
                        <div className="text-xs text-green-400 text-center flex items-center justify-center gap-1">
                          <CheckCircle className="h-3 w-3" />
                          NASA-JPL Precision
                        </div>
                      </div>
                    )}
                  </motion.div>
                );
              })}
            </div>
          </CardContent>
        </Card>

        {/* Quick Stats */}
        <div className="grid md:grid-cols-4 gap-4">
          <motion.div whileHover={{ scale: 1.05 }} transition={{ duration: 0.2 }}>
            <Card className="bg-cosmic-mid/10 border-cosmic-mid/20 cursor-pointer">
              <CardContent className="p-6 text-center">
                <div className="text-3xl font-bold text-cosmic-bright mb-2">
                  {chartData.rareYogas?.length || 0}
                </div>
                <div className="text-sm text-muted-foreground">Rare Yogas</div>
                <div className="text-xs text-cosmic-bright/60 mt-1">
                  {chartData.rareYogas?.length > 0 ? 'Exceptional combinations' : 'Standard patterns'}
                </div>
              </CardContent>
            </Card>
          </motion.div>
          
          <motion.div whileHover={{ scale: 1.05 }} transition={{ duration: 0.2 }}>
            <Card className="bg-celestial-mid/10 border-celestial-mid/20 cursor-pointer">
              <CardContent className="p-6 text-center">
                <div className="text-3xl font-bold text-celestial-bright mb-2">
                  {chartData.personalizedInsights?.length || 0}
                </div>
                <div className="text-sm text-muted-foreground">Personal Insights</div>
                <div className="text-xs text-celestial-bright/60 mt-1">
                  AI-powered analysis
                </div>
              </CardContent>
            </Card>
          </motion.div>
          
          <motion.div whileHover={{ scale: 1.05 }} transition={{ duration: 0.2 }}>
            <Card className="bg-mystical-mid/10 border-mystical-mid/20 cursor-pointer">
              <CardContent className="p-6 text-center">
                <div className="text-3xl font-bold text-mystical-bright mb-2">
                  {chartData.dashaTable?.length || 0}
                </div>
                <div className="text-sm text-muted-foreground">Dasha Periods</div>
                <div className="text-xs text-mystical-bright/60 mt-1">
                  Life phase timeline
                </div>
              </CardContent>
            </Card>
          </motion.div>
          
          <motion.div whileHover={{ scale: 1.05 }} transition={{ duration: 0.2 }}>
            <Card className="bg-green-500/10 border-green-500/20 cursor-pointer">
              <CardContent className="p-6 text-center">
                <div className="text-3xl font-bold text-green-400 mb-2">
                  {chartData.personalizedRemedies?.length || 0}
                </div>
                <div className="text-sm text-muted-foreground">Remedies</div>
                <div className="text-xs text-green-400/60 mt-1">
                  Cosmic healing
                </div>
              </CardContent>
            </Card>
          </motion.div>
        </div>

        {/* AI Personality Analysis */}
        <Card className="bg-card/80 backdrop-blur-sm border-cosmic-mid/20">
          <CardHeader>
            <CardTitle className="flex items-center gap-2 text-cosmic-bright">
              <Brain className="h-5 w-5" />
              AI Personality Analysis
            </CardTitle>
          </CardHeader>
          <CardContent>
            <p className="text-muted-foreground leading-relaxed text-lg">
              {chartData.aiPersonalityAnalysis}
            </p>
          </CardContent>
        </Card>

        {/* Current Cosmic Weather */}
        {chartData.currentCosmicWeather && (
          <Card className="bg-card/80 backdrop-blur-sm border-celestial-mid/20">
            <CardHeader>
              <CardTitle className="flex items-center gap-2 text-celestial-bright">
                <Activity className="h-5 w-5" />
                Current Cosmic Weather
              </CardTitle>
            </CardHeader>
            <CardContent>
              <div className="space-y-4">
                <p className="text-lg font-semibold text-foreground">
                  {chartData.currentCosmicWeather.cosmicClimate}
                </p>
                <div>
                  <h4 className="font-semibold text-foreground mb-2">Major Influences:</h4>
                  <ul className="space-y-1">
                    {chartData.currentCosmicWeather.majorInfluences?.map((influence: string, index: number) => (
                      <li key={index} className="text-muted-foreground flex items-start gap-2">
                        <Star className="h-3 w-3 text-celestial-bright mt-1 flex-shrink-0" />
                        {influence}
                      </li>
                    ))}
                  </ul>
                </div>
                <div>
                  <h4 className="font-semibold text-foreground mb-2">Recommendations:</h4>
                  <ul className="space-y-1">
                    {chartData.currentCosmicWeather.recommendations?.map((rec: string, index: number) => (
                      <li key={index} className="text-muted-foreground flex items-start gap-2">
                        <CheckCircle className="h-3 w-3 text-green-400 mt-1 flex-shrink-0" />
                        {rec}
                      </li>
                    ))}
                  </ul>
                </div>
              </div>
            </CardContent>
          </Card>
        )}
      </div>
    );
  }, [chartData]);

  // ✅ **SIMPLIFIED INSIGHTS RENDERING - NO SUB-CATEGORIES**
  const renderPersonalizedInsights = useMemo(() => {
    if (!chartData?.personalizedInsights?.length) {
      return (
        <Card className="bg-card/80 backdrop-blur-sm border-celestial-mid/20">
          <CardContent className="p-8 text-center">
            <Brain className="h-12 w-12 text-celestial-bright mx-auto mb-4" />
            <h3 className="text-xl font-semibold mb-2">Personalized Insights</h3>
            <p className="text-muted-foreground">Your personalized insights are being generated...</p>
          </CardContent>
        </Card>
      );
    }

    return (
      <div className="space-y-6">
        {chartData.personalizedInsights
          .sort((a: any, b: any) => b.priority - a.priority)
          .map((insight: any, index: number) => (
          <motion.div
            key={insight.id}
            initial={{ opacity: 0, y: 20 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ delay: index * 0.1, duration: 0.5 }}
          >
            <Card className="bg-card/80 backdrop-blur-sm border-cosmic-mid/20 hover:border-cosmic-mid/40 transition-all duration-300 overflow-hidden group cursor-pointer">
              <CardContent className="p-6">
                <div className="flex items-start gap-4">
                  <div className="w-12 h-12 rounded-lg bg-cosmic-mid/20 flex items-center justify-center group-hover:scale-110 transition-transform">
                    <span className="text-cosmic-bright">
                      {PersonalizedChartUtils.getInsightIcon(insight.category)}
                    </span>
                  </div>
                  
                  <div className="flex-1">
                    <div className="flex items-start justify-between mb-3">
                      <h3 className="font-playfair font-bold text-xl text-foreground">{insight.title}</h3>
                      <div className="flex gap-2">
                        <Badge className={PersonalizedChartUtils.getPriorityBadgeColor(insight.priority)}>
                          Priority {insight.priority}
                        </Badge>
                        <Badge className="bg-green-500/20 text-green-400 border-green-500/20">
                          {PersonalizedChartUtils.formatConfidence(insight.confidence)} Confidence
                        </Badge>
                      </div>
                    </div>
                    
                    <p className="text-muted-foreground mb-4 leading-relaxed text-lg">
                      {insight.insight}
                    </p>
                    
                    {insight.actionableSteps?.length > 0 && (
                      <div className="space-y-3">
                        <h4 className="font-semibold text-foreground flex items-center gap-2">
                          <Target className="h-4 w-4 text-cosmic-bright" />
                          Actionable Steps
                        </h4>
                        <div className="grid gap-2">
                          {insight.actionableSteps.map((step: string, stepIndex: number) => (
                            <motion.div
                              key={stepIndex}
                              initial={{ opacity: 0, x: -10 }}
                              animate={{ opacity: 1, x: 0 }}
                              transition={{ delay: (index * 0.1) + (stepIndex * 0.05) }}
                              className="flex items-start gap-2 text-sm text-muted-foreground"
                            >
                              <CheckCircle className="h-3 w-3 text-cosmic-bright mt-1 flex-shrink-0" />
                              {step}
                            </motion.div>
                          ))}
                        </div>
                      </div>
                    )}
                    
                    <div className="mt-4 pt-4 border-t border-cosmic-mid/20 flex flex-wrap gap-4 text-sm">
                      <div className="flex items-center gap-2">
                        <Clock className="h-3 w-3 text-muted-foreground" />
                        <span className="text-muted-foreground">Timeline: {insight.expectedManifestationTime}</span>
                      </div>
                      {insight.planetaryBasis?.length > 0 && (
                        <div className="flex items-center gap-2">
                          <Stars className="h-3 w-3 text-muted-foreground" />
                          <span className="text-muted-foreground">Planetary Basis: {insight.planetaryBasis.join(', ')}</span>
                        </div>
                      )}
                    </div>
                  </div>
                </div>
              </CardContent>
            </Card>
          </motion.div>
        ))}
      </div>
    );
  }, [chartData]);

  // ✅ **COSMIC PERSONALITY RENDERING**
  const renderCosmicPersonality = useMemo(() => {
    if (!chartData?.cosmicPersonality) {
      return (
        <Card className="bg-card/80 backdrop-blur-sm border-mystical-mid/20">
          <CardContent className="p-8 text-center">
            <User className="h-12 w-12 text-mystical-bright mx-auto mb-4" />
            <h3 className="text-xl font-semibold mb-2">Cosmic Personality</h3>
            <p className="text-muted-foreground">Your cosmic personality analysis is being generated...</p>
          </CardContent>
        </Card>
      );
    }

    const personality = chartData.cosmicPersonality;

    return (
      <div className="space-y-8">
        <motion.div
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ duration: 0.6 }}
        >
          <Card className="bg-gradient-cosmic overflow-hidden">
            <div className="absolute inset-0 bg-black/20" />
            <CardContent className="relative z-10 p-8 text-center">
              <motion.div
                initial={{ scale: 0 }}
                animate={{ scale: 1 }}
                transition={{ type: "spring", duration: 0.6 }}
                className="flex justify-center mb-6"
              >
                <Stars className="h-16 w-16 text-white" />
              </motion.div>
              
              <h2 className="text-3xl font-playfair font-bold text-white mb-4">
                Your Soul Archetype: {personality.soulArchetype}
              </h2>
              <p className="text-white/90 text-lg leading-relaxed max-w-3xl mx-auto mb-6">
                {personality.coreEssence}
              </p>
              
              <Badge className="bg-white/20 text-white border-white/30 text-lg px-4 py-2">
                <Crown className="h-4 w-4 mr-2" />
                {personality.evolutionaryStage}
              </Badge>
            </CardContent>
          </Card>
        </motion.div>

        <div className="grid md:grid-cols-2 gap-6">
          <motion.div
            initial={{ opacity: 0, x: -20 }}
            animate={{ opacity: 1, x: 0 }}
            transition={{ duration: 0.6, delay: 0.2 }}
          >
            <Card className="bg-card/80 backdrop-blur-sm border-celestial-mid/20 h-full">
              <CardHeader>
                <CardTitle className="flex items-center gap-2 text-celestial-bright">
                  <Target className="h-5 w-5" />
                  Soul Mission
                </CardTitle>
              </CardHeader>
              <CardContent className="space-y-4">
                <p className="text-muted-foreground leading-relaxed">
                  {personality.soulMission}
                </p>
                <div>
                  <h4 className="font-semibold text-foreground mb-2">Life Theme</h4>
                  <p className="text-sm text-muted-foreground">{personality.lifeTheme}</p>
                </div>
                <div>
                  <h4 className="font-semibold text-foreground mb-2">Spiritual Path</h4>
                  <p className="text-sm text-muted-foreground">{personality.spiritualPath}</p>
                </div>
              </CardContent>
            </Card>
          </motion.div>

          <motion.div
            initial={{ opacity: 0, x: 20 }}
            animate={{ opacity: 1, x: 0 }}
            transition={{ duration: 0.6, delay: 0.3 }}
          >
            <Card className="bg-card/80 backdrop-blur-sm border-mystical-mid/20 h-full">
              <CardHeader>
                <CardTitle className="flex items-center gap-2 text-mystical-bright">
                  <Gift className="h-5 w-5" />
                  Cosmic Gifts
                </CardTitle>
              </CardHeader>
              <CardContent className="space-y-4">
                <div className="grid gap-2">
                  {personality.cosmicGifts?.map((gift: string, index: number) => (
                    <motion.div
                      key={index}
                      initial={{ opacity: 0, x: -10 }}
                      animate={{ opacity: 1, x: 0 }}
                      transition={{ delay: 0.4 + (index * 0.05) }}
                      className="flex items-center gap-2 text-sm"
                    >
                      <Star className="h-3 w-3 text-mystical-bright flex-shrink-0" />
                      <span className="text-muted-foreground">{gift}</span>
                    </motion.div>
                  )) || []}
                </div>
              </CardContent>
            </Card>
          </motion.div>
        </div>

        {showAdvancedFeatures && (
          <motion.div
            initial={{ opacity: 0, height: 0 }}
            animate={{ opacity: 1, height: 'auto' }}
            transition={{ duration: 0.5 }}
            className="grid md:grid-cols-2 gap-6"
          >
            <Card className="bg-card/80 backdrop-blur-sm border-cosmic-mid/20">
              <CardHeader>
                <CardTitle className="flex items-center gap-2 text-cosmic-bright">
                  <Eye className="h-5 w-5" />
                  Hidden Talents
                </CardTitle>
              </CardHeader>
              <CardContent>
                <div className="grid gap-2">
                  {personality.hiddenTalents?.map((talent: string, index: number) => (
                    <div key={index} className="flex items-center gap-2 text-sm">
                      <Diamond className="h-3 w-3 text-cosmic-bright flex-shrink-0" />
                      <span className="text-muted-foreground">{talent}</span>
                    </div>
                  )) || []}
                </div>
              </CardContent>
            </Card>

            <Card className="bg-card/80 backdrop-blur-sm border-orange-400/20">
              <CardHeader>
                <CardTitle className="flex items-center gap-2 text-orange-400">
                  <BookOpen className="h-5 w-5" />
                  Karma Lessons
                </CardTitle>
              </CardHeader>
              <CardContent>
                <div className="grid gap-2">
                  {personality.karmaLessons?.map((lesson: string, index: number) => (
                    <div key={index} className="flex items-center gap-2 text-sm">
                      <Lightbulb className="h-3 w-3 text-orange-400 flex-shrink-0" />
                      <span className="text-muted-foreground">{lesson}</span>
                    </div>
                  )) || []}
                </div>
              </CardContent>
            </Card>
          </motion.div>
        )}
      </div>
    );
  }, [chartData, showAdvancedFeatures]);

  // ✅ **REMEDIES RENDERING**
  const renderRemedies = useMemo(() => {
    if (!chartData?.personalizedRemedies?.length) {
      return (
        <Card className="bg-card/80 backdrop-blur-sm border-green-500/20">
          <CardContent className="p-8 text-center">
            <Sparkle className="h-12 w-12 text-green-400 mx-auto mb-4" />
            <h3 className="text-xl font-semibold mb-2">Personalized Remedies</h3>
            <p className="text-muted-foreground">Your personalized remedies are being generated...</p>
          </CardContent>
        </Card>
      );
    }

    return (
      <div className="space-y-6">
        {chartData.personalizedRemedies.map((remedy: any, index: number) => (
          <motion.div
            key={index}
            initial={{ opacity: 0, y: 20 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ delay: index * 0.1, duration: 0.5 }}
          >
            <Card className="bg-card/80 backdrop-blur-sm border-green-500/20 hover:border-green-500/40 transition-all duration-300">
              <CardContent className="p-6">
                <div className="flex items-start gap-4">
                  <div className="w-12 h-12 rounded-lg bg-green-500/20 flex items-center justify-center">
                    <Sparkle className="h-6 w-6 text-green-400" />
                  </div>
                  <div className="flex-1">
                    <h3 className="font-playfair font-bold text-xl text-foreground mb-2">
                      {remedy.title || `Remedy ${index + 1}`}
                    </h3>
                    <p className="text-muted-foreground mb-4 leading-relaxed">
                      {remedy.description || remedy.remedy}
                    </p>
                    {remedy.instructions && (
                      <div className="mt-4 p-3 bg-green-500/10 rounded-lg">
                        <h4 className="font-semibold text-foreground mb-2">Instructions:</h4>
                        <p className="text-sm text-muted-foreground">{remedy.instructions}</p>
                      </div>
                    )}
                  </div>
                </div>
              </CardContent>
            </Card>
          </motion.div>
        ))}
      </div>
    );
  }, [chartData]);

  // Authentication guard
  if (!isAuthenticated) {
    return (
      <div className="min-h-screen bg-background text-foreground flex items-center justify-center">
        <CosmicBackground />
        <Card className="bg-card/80 backdrop-blur-sm border-mystical-mid/20 p-8 text-center max-w-md">
          <CardContent>
            <motion.div
              animate={{ rotate: 360 }}
              transition={{ duration: 2, repeat: Infinity, ease: "linear" }}
              className="flex justify-center mb-4"
            >
              <Atom className="h-8 w-8 text-mystical-bright" />
            </motion.div>
            <h2 className="text-xl font-playfair font-bold mb-2">Accessing Cosmic Database</h2>
            <p className="text-muted-foreground">Verifying your celestial credentials...</p>
          </CardContent>
        </Card>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-background text-foreground" ref={containerRef}>
      <CosmicBackground />
      <Navigation />
      
      <main className="pt-20 pb-16 px-6">
        <div className="max-w-7xl mx-auto">
          
          {/* Enhanced Hero Section */}
          <motion.div
            initial={{ opacity: 0, y: 30 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ duration: 0.8 }}
            className="text-center mb-12"
          >
            <div className="flex justify-center items-center gap-4 mb-6">
              <motion.div 
                className="p-4 rounded-full bg-gradient-celestial"
                whileHover={{ scale: 1.1, rotate: 360 }}
                transition={{ duration: 0.5 }}
              >
                <Crown className="h-12 w-12 text-white" />
              </motion.div>
              {currentUser && (
                <div className="text-left">
                  <h1 className="text-3xl md:text-4xl font-playfair font-bold text-foreground">
                    Welcome to Your Personal Cosmic Universe, {currentUser.firstName || currentUser.username}!
                  </h1>
                  <div className="flex items-center gap-4 mt-2">
                    <Badge variant="secondary" className="bg-cosmic-mid/20 text-cosmic-bright">
                      <Award className="h-3 w-3 mr-1" />
                      Premium Member
                    </Badge>
                    <Badge variant="secondary" className="bg-celestial-mid/20 text-celestial-bright">
                      <Star className="h-3 w-3 mr-1" />
                      Advanced Features Unlocked
                    </Badge>
                  </div>
                </div>
              )}
            </div>
            
            <p className="text-muted-foreground text-lg max-w-4xl mx-auto">
              Experience the most advanced personalized Vedic astrology analysis available anywhere. 
              Powered by Swiss Ephemeris precision, AI enhancement, and 500+ rare yoga detection, 
              this is your complete cosmic blueprint for life transformation.
            </p>
            
            {/* Unique Features Showcase */}
            <div className="flex justify-center gap-4 mt-8 flex-wrap">
              {[
                { icon: <Brain className="h-4 w-4" />, text: "AI Personality Analysis", color: "cosmic" },
                { icon: <Telescope className="h-4 w-4" />, text: "Predictive Insights", color: "celestial" },
                { icon: <Heart className="h-4 w-4" />, text: "Cosmic Compatibility", color: "mystical" },
                { icon: <InfinityIcon className="h-4 w-4" />, text: "Soul Evolution Guide", color: "cosmic" }
              ].map((feature, index) => (
                <motion.div
                  key={index}
                  initial={{ opacity: 0, scale: 0.8 }}
                  animate={{ opacity: 1, scale: 1 }}
                  transition={{ delay: index * 0.1, duration: 0.5 }}
                  className={`flex items-center gap-2 px-4 py-2 rounded-full 
                    ${feature.color === 'cosmic' ? 'bg-cosmic-mid/20 text-cosmic-bright border border-cosmic-mid/30' : ''}
                    ${feature.color === 'celestial' ? 'bg-celestial-mid/20 text-celestial-bright border border-celestial-mid/30' : ''}
                    ${feature.color === 'mystical' ? 'bg-mystical-mid/20 text-mystical-bright border border-mystical-mid/30' : ''}
                    hover:scale-105 transition-all duration-300 cursor-pointer backdrop-blur-sm`}
                  whileHover={{ scale: 1.05, y: -2 }}
                  whileTap={{ scale: 0.95 }}
                >
                  <motion.div
                    animate={{ rotate: [0, 5, -5, 0] }}
                    transition={{ duration: 2, repeat: Infinity, repeatDelay: 3 }}
                  >
                    {feature.icon}
                  </motion.div>
                  <span className="text-sm font-medium whitespace-nowrap">{feature.text}</span>
                </motion.div>
              ))}
            </div>
          </motion.div>

          {/* ✅ Birth Data Form Section */}
          {!chartData && !isLoading && (
            <motion.div
              initial={{ opacity: 0, y: 20 }}
              animate={{ opacity: 1, y: 0 }}
              transition={{ duration: 0.6, delay: 0.3 }}
            >
              <Card className="bg-card/90 backdrop-blur-sm border-cosmic-mid/20 max-w-4xl mx-auto">
                <CardHeader className="text-center">
                  <CardTitle className="text-2xl font-playfair font-bold text-foreground flex items-center justify-center gap-2">
                    <Sparkles className="h-6 w-6 text-cosmic-bright" />
                    Complete Your Cosmic Profile
                  </CardTitle>
                  <CardDescription className="text-muted-foreground text-lg">
                    Provide your complete birth information for the most accurate personalized cosmic analysis available anywhere
                  </CardDescription>
                </CardHeader>
                <CardContent className="space-y-6">
                  {error && (
                    <Alert className="border-red-500/50 bg-red-500/10">
                      <AlertCircle className="h-4 w-4 text-red-400" />
                      <AlertDescription className="text-red-300 font-medium">
                        {error}
                      </AlertDescription>
                    </Alert>
                  )}

                  <div className="grid md:grid-cols-2 gap-6">
                    {/* Personal Information */}
                    <div className="space-y-4">
                      <h3 className="font-semibold text-foreground flex items-center gap-2">
                        <User className="h-4 w-4 text-cosmic-bright" />
                        Personal Information
                      </h3>
                      
                      <div className="space-y-2">
                        <Label htmlFor="name">Complete Name *</Label>
                        <Input
                          id="name"
                          type="text"
                          placeholder="Enter your full name"
                          value={birthData.name}
                          onChange={(e) => handleInputChange('name', e.target.value)}
                          className="bg-card/50 border-cosmic-mid/30 focus:border-cosmic-bright"
                          required
                        />
                      </div>

                      <div className="space-y-2">
                        <Label htmlFor="gender">Gender</Label>
                        <Select value={birthData.gender} onValueChange={(value) => handleInputChange('gender', value)}>
                          <SelectTrigger className="bg-card/50 border-cosmic-mid/30 focus:border-cosmic-bright">
                            <SelectValue placeholder="Select gender" />
                          </SelectTrigger>
                          <SelectContent>
                            <SelectItem value="male">Male</SelectItem>
                            <SelectItem value="female">Female</SelectItem>
                            <SelectItem value="other">Other/Prefer not to say</SelectItem>
                          </SelectContent>
                        </Select>
                      </div>
                    </div>

                    {/* Birth Information */}
                    <div className="space-y-4">
                      <h3 className="font-semibold text-foreground flex items-center gap-2">
                        <Calendar className="h-4 w-4 text-celestial-bright" />
                        Birth Information
                      </h3>
                      
                      <div className="space-y-2">
                        <Label htmlFor="birthDateTime">Birth Date & Time *</Label>
                        <Input
                          id="birthDateTime"
                          type="datetime-local"
                          value={birthData.birthDateTime}
                          onChange={(e) => handleInputChange('birthDateTime', e.target.value)}
                          className="bg-card/50 border-cosmic-mid/30 focus:border-cosmic-bright"
                          required
                        />
                        <p className="text-xs text-muted-foreground">
                          Exact birth time is crucial for accurate analysis
                        </p>
                      </div>

                      <div className="space-y-2">
                        <Label htmlFor="timezone">Timezone</Label>
                        <Select value={birthData.timezone} onValueChange={(value) => handleInputChange('timezone', value)}>
                          <SelectTrigger className="bg-card/50 border-cosmic-mid/30 focus:border-cosmic-bright">
                            <SelectValue />
                          </SelectTrigger>
                          <SelectContent>
                            <SelectItem value="Asia/Kolkata">Asia/Kolkata (IST)</SelectItem>
                            <SelectItem value="America/New_York">America/New_York (EST)</SelectItem>
                            <SelectItem value="America/Los_Angeles">America/Los_Angeles (PST)</SelectItem>
                            <SelectItem value="Europe/London">Europe/London (GMT)</SelectItem>
                            <SelectItem value="Australia/Sydney">Australia/Sydney (AEST)</SelectItem>
                          </SelectContent>
                        </Select>
                      </div>
                    </div>
                  </div>

                  {/* Location Information */}
                  <div className="space-y-4">
                    <h3 className="font-semibold text-foreground flex items-center gap-2">
                      <MapPin className="h-4 w-4 text-mystical-bright" />
                      Birth Location & Coordinates
                    </h3>
                    
                    <div className="grid md:grid-cols-3 gap-4">
                      <div className="space-y-2">
                        <Label htmlFor="birthLocation">Birth City/Location *</Label>
                        <Input
                          id="birthLocation"
                          type="text"
                          placeholder="e.g., New Delhi, India"
                          value={birthData.birthLocation}
                          onChange={(e) => handleInputChange('birthLocation', e.target.value)}
                          className="bg-card/50 border-cosmic-mid/30 focus:border-cosmic-bright"
                          required
                        />
                      </div>
                      
                      <div className="space-y-2">
                        <Label htmlFor="birthLatitude">Latitude *</Label>
                        <Input
                          id="birthLatitude"
                          type="number"
                          step="0.000001"
                          placeholder="e.g., 28.6139"
                          value={birthData.birthLatitude || ''}
                          onChange={(e) => handleInputChange('birthLatitude', parseFloat(e.target.value) || null)}
                          className="bg-card/50 border-cosmic-mid/30 focus:border-cosmic-bright"
                          required
                        />
                      </div>
                      
                      <div className="space-y-2">
                        <Label htmlFor="birthLongitude">Longitude *</Label>
                        <Input
                          id="birthLongitude"
                          type="number"
                          step="0.000001"
                          placeholder="e.g., 77.2090"
                          value={birthData.birthLongitude || ''}
                          onChange={(e) => handleInputChange('birthLongitude', parseFloat(e.target.value) || null)}
                          className="bg-card/50 border-cosmic-mid/30 focus:border-cosmic-bright"
                          required
                        />
                      </div>
                    </div>
                    
                    <p className="text-xs text-muted-foreground">
                      💡 You can find exact coordinates using Google Maps. Right-click on your birth location and select "What's here?"
                    </p>
                  </div>

                  {/* Generate Button */}
                  <div className="flex justify-center pt-6">
                    <Button
                      onClick={generatePersonalizedChart}
                      disabled={isLoading}
                      size="lg"
                      className="bg-gradient-cosmic hover:bg-gradient-celestial text-white font-semibold px-8 py-3 rounded-lg shadow-lg hover:shadow-xl transition-all duration-300"
                    >
                      {isLoading ? (
                        <>
                          <Loader2 className="mr-2 h-5 w-5 animate-spin" />
                          Generating Your Cosmic Blueprint...
                        </>
                      ) : (
                        <>
                          <Crown className="mr-2 h-5 w-5" />
                          Generate My Personalized Chart
                        </>
                      )}
                    </Button>
                  </div>
                </CardContent>
              </Card>
            </motion.div>
          )}

          {/* ✅ Loading State */}
          {isLoading && (
            <motion.div
              initial={{ opacity: 0 }}
              animate={{ opacity: 1 }}
              className="max-w-2xl mx-auto"
            >
              <Card className="bg-card/90 backdrop-blur-sm border-cosmic-mid/20">
                <CardContent className="p-8 text-center space-y-6">
                  <motion.div
                    animate={{ 
                      rotate: 360,
                      scale: [1, 1.1, 1],
                    }}
                    transition={{ 
                      rotate: { duration: 3, repeat: Infinity, ease: "linear" },
                      scale: { duration: 2, repeat: Infinity }
                    }}
                    className="flex justify-center"
                  >
                    <div className="relative">
                      <Crown className="h-16 w-16 text-cosmic-bright" />
                      <motion.div
                        animate={{ opacity: [0.3, 1, 0.3] }}
                        transition={{ duration: 1.5, repeat: Infinity }}
                        className="absolute inset-0 rounded-full bg-cosmic-bright/20 blur-xl"
                      />
                    </div>
                  </motion.div>
                  
                  <div className="space-y-3">
                    <h2 className="text-2xl font-playfair font-bold text-foreground">
                      Generating Your Cosmic Blueprint
                    </h2>
                    <p className="text-muted-foreground">
                      {loadingStage || 'Initializing world-class personalized analysis...'}
                    </p>
                  </div>

                  <div className="space-y-2">
                    <div className="flex justify-between text-sm">
                      <span className="text-muted-foreground">Progress</span>
                      <span className="text-cosmic-bright font-medium">{loadingProgress}%</span>
                    </div>
                    <Progress 
                      value={loadingProgress} 
                      className="w-full h-3"
                    />
                  </div>

                  <div className="flex justify-center gap-4 text-xs text-muted-foreground">
                    <div className="flex items-center gap-1">
                      <Telescope className="h-3 w-3" />
                      Swiss Ephemeris
                    </div>
                    <div className="flex items-center gap-1">
                      <Brain className="h-3 w-3" />
                      AI Enhancement
                    </div>
                    <div className="flex items-center gap-1">
                      <Star className="h-3 w-3" />
                      500+ Yogas
                    </div>
                  </div>
                </CardContent>
              </Card>
            </motion.div>
          )}

          {/* ✅ MAIN CHART DISPLAY - CLEAN PRODUCTION VERSION */}
          {chartData && !isLoading && (
            <motion.div
              initial={{ opacity: 0, y: 20 }}
              animate={{ opacity: 1, y: 0 }}
              transition={{ duration: 0.6 }}
              className="space-y-8"
            >
              {/* Chart Header */}
              <Card className="bg-gradient-mystical overflow-hidden">
                <div className="absolute inset-0 bg-black/30" />
                <CardContent className="relative z-10 p-8 text-center text-white">
                  <motion.div
                    animate={{ 
                      scale: [1, 1.05, 1],
                      rotate: [0, 5, -5, 0]
                    }}
                    transition={{ duration: 4, repeat: Infinity }}
                    className="flex justify-center mb-4"
                  >
                    <Crown className="h-12 w-12" />
                  </motion.div>
                  
                  <h1 className="text-3xl font-playfair font-bold mb-2">
                    Your Personalized Cosmic Blueprint
                  </h1>
                  <p className="text-white/90 mb-4">
                    Generated with Swiss Ephemeris precision and AI enhancement
                  </p>
                  
                  <div className="flex justify-center gap-4 flex-wrap">
                    <Badge className="bg-white/20 text-white border-white/30">
                      <Star className="h-3 w-3 mr-1" />
                      Uniqueness: {chartData.uniquenessScore}%
                    </Badge>
                    <Badge className="bg-white/20 text-white border-white/30">
                      <Atom className="h-3 w-3 mr-1" />
                      ID: {chartData.cosmicFingerprint}
                    </Badge>
                    <Badge className="bg-white/20 text-white border-white/30">
                      <Award className="h-3 w-3 mr-1" />
                      {chartData.calculationMetadata?.accuracy}
                    </Badge>
                  </div>
                </CardContent>
              </Card>

              {/* ✅ PERFECT TAB SYSTEM - NO DEBUG ELEMENTS */}
              <div className="w-full space-y-8">
                
                {/* ✅ CLEAN TAB BUTTONS */}
                <div className="bg-card/50 p-2 rounded-lg">
                  <div className="grid grid-cols-2 md:grid-cols-6 gap-2">
                    {[
                      { id: 'overview', label: 'Overview', icon: <Eye className="h-4 w-4" />, color: 'cosmic' },
                      { id: 'insights', label: 'Insights', icon: <Brain className="h-4 w-4" />, color: 'celestial' },
                      { id: 'personality', label: 'Personality', icon: <User className="h-4 w-4" />, color: 'mystical' },
                      { id: 'yogas', label: 'Yogas', icon: <Star className="h-4 w-4" />, color: 'cosmic' },
                      { id: 'dasha', label: 'Dasha', icon: <BarChart2 className="h-4 w-4" />, color: 'celestial' },
                      { id: 'chart', label: 'Chart', icon: <Globe className="h-4 w-4" />, color: 'mystical' },
                      { id: 'remedies', label: 'Remedies', icon: <Sparkle className="h-4 w-4" />, color: 'green' }
                    ].map((tab) => (
                      <button
                        key={tab.id}
                        onClick={(e) => {
                          e.preventDefault();
                          e.stopPropagation();
                          setActiveTab(tab.id);
                        }}
                        className={`
                          relative p-3 rounded-lg font-medium transition-all duration-200 
                          flex items-center justify-center gap-2 min-h-[48px]
                          border-2 cursor-pointer select-none
                          hover:scale-105 active:scale-95
                          ${activeTab === tab.id 
                            ? `${tab.color === 'cosmic' ? 'bg-cosmic-mid text-white border-cosmic-mid shadow-lg' : ''}
                               ${tab.color === 'celestial' ? 'bg-celestial-mid text-white border-celestial-mid shadow-lg' : ''}
                               ${tab.color === 'mystical' ? 'bg-mystical-mid text-white border-mystical-mid shadow-lg' : ''}
                               ${tab.color === 'green' ? 'bg-green-500 text-white border-green-500 shadow-lg' : ''}`
                            : 'bg-transparent text-foreground border-transparent hover:bg-muted/50'
                          }
                        `}
                        style={{ 
                          pointerEvents: 'auto',
                          zIndex: 10,
                          position: 'relative'
                        }}
                      >
                        {tab.icon}
                        <span className="hidden sm:inline">{tab.label}</span>
                        <span className="sm:hidden">{tab.label.slice(0, 4)}</span>
                        
                        {/* ✅ Active Indicator */}
                        {activeTab === tab.id && (
                          <motion.div
                            layoutId="activeTab"
                            className="absolute inset-0 rounded-lg border-2 border-white/30"
                            initial={{ opacity: 0 }}
                            animate={{ opacity: 1 }}
                            transition={{ duration: 0.3 }}
                          />
                        )}
                      </button>
                    ))}
                  </div>
                </div>

                {/* ✅ CLEAN TAB CONTENT SECTIONS - NO DEBUG MESSAGES */}
                <div className="min-h-[400px]">
                  {/* Overview Tab */}
                  {activeTab === 'overview' && (
                    <motion.div
                      key="overview"
                      initial={{ opacity: 0, x: -20 }}
                      animate={{ opacity: 1, x: 0 }}
                      exit={{ opacity: 0, x: 20 }}
                      transition={{ duration: 0.3 }}
                    >
                      {renderChartOverview}
                    </motion.div>
                  )}

                  {/* Insights Tab */}
                  {activeTab === 'insights' && (
                    <motion.div
                      key="insights"
                      initial={{ opacity: 0, x: -20 }}
                      animate={{ opacity: 1, x: 0 }}
                      exit={{ opacity: 0, x: 20 }}
                      transition={{ duration: 0.3 }}
                    >
                      {renderPersonalizedInsights}
                    </motion.div>
                  )}

                  {/* Personality Tab */}
                  {activeTab === 'personality' && (
                    <motion.div
                      key="personality"
                      initial={{ opacity: 0, x: -20 }}
                      animate={{ opacity: 1, x: 0 }}
                      exit={{ opacity: 0, x: 20 }}
                      transition={{ duration: 0.3 }}
                    >
                      {renderCosmicPersonality}
                      <div className="flex justify-center mt-6">
                        <Button
                          variant="outline"
                          onClick={() => setShowAdvancedFeatures(!showAdvancedFeatures)}
                          className="border-mystical-mid/30 hover:bg-mystical-mid/20"
                        >
                          {showAdvancedFeatures ? <EyeOff className="h-4 w-4 mr-2" /> : <Eye className="h-4 w-4 mr-2" />}
                          {showAdvancedFeatures ? 'Hide' : 'Show'} Advanced Features
                        </Button>
                      </div>
                    </motion.div>
                  )}

                  {/* Yogas Tab */}
                  {activeTab === 'yogas' && (
                    <motion.div
                      key="yogas"
                      initial={{ opacity: 0, x: -20 }}
                      animate={{ opacity: 1, x: 0 }}
                      exit={{ opacity: 0, x: 20 }}
                      transition={{ duration: 0.3 }}
                    >
                      {chartData.rareYogas && chartData.rareYogas.length > 0 ? (
                        <RareYogaHighlights yogas={chartData.rareYogas} />
                      ) : (
                        <Card className="bg-card/80 backdrop-blur-sm border-cosmic-mid/20">
                          <CardContent className="p-8 text-center">
                            <Star className="h-12 w-12 text-cosmic-bright mx-auto mb-4" />
                            <h3 className="text-xl font-semibold mb-2">Yoga Analysis</h3>
                            <p className="text-muted-foreground">Your unique yoga combinations are being analyzed...</p>
                          </CardContent>
                        </Card>
                      )}
                    </motion.div>
                  )}

                  {/* ✅ Dasha Tab */}
                  {activeTab === 'dasha' && (
                    <motion.div
                      key="dasha"
                      initial={{ opacity: 0, x: -20 }}
                      animate={{ opacity: 1, x: 0 }}
                      exit={{ opacity: 0, x: 20 }}
                      transition={{ duration: 0.3 }}
                    >
                      {chartData.dashaTable && chartData.dashaTable.length > 0 ? (
                        <DashaTimeline dashaTable={chartData.dashaTable} />
                      ) : (
                        <Card className="bg-card/80 backdrop-blur-sm border-celestial-mid/20">
                          <CardContent className="p-8 text-center">
                            <BarChart2 className="h-12 w-12 text-celestial-bright mx-auto mb-4" />
                            <h3 className="text-xl font-semibold mb-2">Dasha Timeline</h3>
                            <p className="text-muted-foreground">Your dasha timeline is being generated...</p>
                          </CardContent>
                        </Card>
                      )}
                    </motion.div>
                  )}

                  {/* ✅ Chart Visualizer Tab */}
                  {activeTab === 'chart' && (
                    <motion.div
                      key="chart"
                      initial={{ opacity: 0, x: -20 }}
                      animate={{ opacity: 1, x: 0 }}
                      exit={{ opacity: 0, x: 20 }}
                      transition={{ duration: 0.3 }}
                    >
                      {chartData ? (
                        <UniqueVedicChartVisualizer data={chartData} />
                      ) : (
                        <Card className="bg-card/80 backdrop-blur-sm border-mystical-mid/20">
                          <CardContent className="p-8 text-center">
                            <Globe className="h-12 w-12 text-mystical-bright mx-auto mb-4" />
                            <h3 className="text-xl font-semibold mb-2">Vedic Chart Visualization</h3>
                            <p className="text-muted-foreground">Your chart visualization is being generated...</p>
                          </CardContent>
                        </Card>
                      )}
                    </motion.div>
                  )}

                  {/* ✅ Remedies Tab */}
                  {activeTab === 'remedies' && (
                    <motion.div
                      key="remedies"
                      initial={{ opacity: 0, x: -20 }}
                      animate={{ opacity: 1, x: 0 }}
                      exit={{ opacity: 0, x: 20 }}
                      transition={{ duration: 0.3 }}
                    >
                      {renderRemedies}
                    </motion.div>
                  )}
                </div>
              </div>

              {/* ✅ FIXED ACTION BUTTONS */}
              <div className="flex justify-center gap-4 pt-8 border-t border-muted/20">
                <Button
                  onClick={handleReset}
                  variant="outline"
                  className="border-cosmic-mid/30 hover:bg-cosmic-mid/20"
                >
                  <RefreshCw className="h-4 w-4 mr-2" />
                  Generate New Chart
                </Button>
                
                <Button
                  onClick={handleDownload}
                  variant="outline"
                  className="border-celestial-mid/30 hover:bg-celestial-mid/20"
                  disabled={!chartData}
                >
                  <Download className="h-4 w-4 mr-2" />
                  Download Report
                </Button>
                
                <Button
                  onClick={handleShare}
                  variant="outline"
                  className="border-mystical-mid/30 hover:bg-mystical-mid/20"
                  disabled={!chartData}
                >
                  <Share2 className="h-4 w-4 mr-2" />
                  Share Chart
                </Button>
              </div>
            </motion.div>
          )}
        </div>
      </main>

      <Footer />
    </div>
  );
}
