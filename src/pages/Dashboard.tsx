import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { Button } from '@/components/ui/button';
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card';
import { CosmicBackground } from '@/components/CosmicBackground';
import { CosmicWeatherCard } from '@/components/CosmicWeatherCard';
import { BirthProfileForm } from '@/components/BirthProfileForm';
import { PlanetaryTicker } from '@/components/PlanetaryTicker';
import { Navigation } from '@/components/Navigation';
import { Footer } from '@/components/Footer';
import { useAuth } from '@/contexts/AuthContext';

import { astrologyService } from '@/services/astrologyService';
import { 
  Star, 
  Moon, 
  Calendar, 
  Sparkles, 
  TrendingUp,
  Heart,
  Briefcase,
  Shield,
  ArrowRight,
  Plus,
  Loader2,
  Activity
} from 'lucide-react';
import { motion } from 'framer-motion';

// Types for the data we'll fetch
interface PersonalizedMessage {
  message: string;
  transitInfluence: string;
  recommendation: string;
  intensity: number;
  dominantPlanet: string;
  luckyColor: string;
  bestTimeOfDay: string;
  moonPhase: string;
}

interface LifeAreaInfluence {
  area: string;
  rating: number;
  insight: string;
  icon: string;
  gradient: string;
}

interface UserStats {
  chartsGenerated: number;
  accuracyRate: number;
  cosmicEnergy: string;
  loginStreak: number;
}

export default function Dashboard() {
  const { user } = useAuth();
  
  // State management
  const [personalizedMessage, setPersonalizedMessage] = useState<PersonalizedMessage | null>(null);
  const [lifeAreaInfluences, setLifeAreaInfluences] = useState<LifeAreaInfluence[]>([]);
  const [userStats, setUserStats] = useState<UserStats | null>(null);
  const [isLoading, setIsLoading] = useState(true);
  const [needsBirthProfile, setNeedsBirthProfile] = useState(false);
  const [error, setError] = useState<string | null>(null);

  // Check if user needs to complete birth profile
  const checkBirthProfileComplete = (message: PersonalizedMessage): boolean => {
    return !message.message.includes('Complete your birth profile') && 
           !message.message.includes('Add your birth') &&
           !message.transitInfluence.includes('Complete birth data needed');
  };

  // Fetch all personalized data
  useEffect(() => {
    const fetchDashboardData = async () => {
      if (!user) return;
      
      setIsLoading(true);
      setError(null);

      try {
        // Fetch personalized message first to check if birth profile is complete
        const messageResponse = await fetch('/api/astrology/personalized-message', {
          headers: {
            'Authorization': `Bearer ${localStorage.getItem('token')}`,
            'Content-Type': 'application/json'
          }
        });

        if (!messageResponse.ok) {
          throw new Error('Failed to fetch personalized message');
        }

        const messageData: PersonalizedMessage = await messageResponse.json();
        setPersonalizedMessage(messageData);

        // Check if birth profile is complete
        if (!checkBirthProfileComplete(messageData)) {
          setNeedsBirthProfile(true);
          setIsLoading(false);
          return;
        }

        // If birth profile is complete, fetch additional data
        const [lifeAreasResponse, statsResponse] = await Promise.all([
          fetch('/api/astrology/life-area-influences', {
            headers: {
              'Authorization': `Bearer ${localStorage.getItem('token')}`,
              'Content-Type': 'application/json'
            }
          }),
          fetch('/api/astrology/user-stats', {
            headers: {
              'Authorization': `Bearer ${localStorage.getItem('token')}`,
              'Content-Type': 'application/json'
            }
          })
        ]);

        if (lifeAreasResponse.ok) {
          const lifeAreasData = await lifeAreasResponse.json();
          setLifeAreaInfluences(lifeAreasData);
        }

        if (statsResponse.ok) {
          const statsData = await statsResponse.json();
          setUserStats(statsData);
        }

      } catch (error) {
        console.error('Failed to fetch dashboard data:', error);
        setError('Failed to load personalized content. Please try again.');
        
        // Show fallback data
        setPersonalizedMessage({
          message: `Welcome back, ${user?.name}! The cosmic energies are flowing harmoniously today.`,
          transitInfluence: "Stable planetary alignments support growth",
          recommendation: "Focus on inner reflection and creative pursuits",
          intensity: 3,
          dominantPlanet: "Sun",
          luckyColor: "White",
          bestTimeOfDay: "Morning",
          moonPhase: "Waxing Crescent"
        });
      } finally {
        setIsLoading(false);
      }
    };

    fetchDashboardData();
  }, [user]);

  // Handle birth profile completion
  const handleBirthProfileComplete = () => {
    setNeedsBirthProfile(false);
    setIsLoading(true);
    // Refetch all data
    window.location.reload();
  };

  // Show birth profile form if needed
  if (needsBirthProfile && !isLoading) {
    return <BirthProfileForm onComplete={handleBirthProfileComplete} />;
  }

  // Show loading state
  if (isLoading) {
    return (
      <div className="min-h-screen bg-background text-foreground flex items-center justify-center">
        <CosmicBackground />
        <div className="text-center">
          <Loader2 className="h-12 w-12 animate-spin text-celestial-mid mx-auto mb-4" />
          <p className="text-muted-foreground">Loading your cosmic insights...</p>
        </div>
      </div>
    );
  }

  const quickActions = [
    {
      title: "Generate Birth Chart",
      description: "Create detailed natal chart analysis",
      href: "/birth-chart",
      icon: <Moon className="h-6 w-6" />,
      color: "mystical"
    },
    {
      title: "Advanced Tools",
      description: "Compatibility, transits, and forecasts",
      href: "/advanced-tools",
      icon: <Sparkles className="h-6 w-6" />,
      color: "celestial"
    },
    {
      title: "Cosmic Transformations",
      description: "Personal growth insights",
      href: "/transformations",
      icon: <TrendingUp className="h-6 w-6" />,
      color: "cosmic"
    }
  ];

  // Map life area influences to display format
  const getLifeAreaIcon = (iconName: string) => {
    switch (iconName) {
      case 'Heart': return <Heart className="h-6 w-6" />;
      case 'Briefcase': return <Briefcase className="h-6 w-6" />;
      case 'Shield': return <Shield className="h-6 w-6" />;
      case 'Sparkles': return <Sparkles className="h-6 w-6" />;
      default: return <Star className="h-6 w-6" />;
    }
  };

  // Default life areas if API data is not available
  const defaultLifeAreas = [
    { 
      area: "Love & Relationships", 
      icon: "Heart", 
      rating: 3,
      insight: "Complete your birth profile for personalized insights",
      gradient: "from-pink-500 to-rose-500"
    },
    { 
      area: "Career & Success", 
      icon: "Briefcase", 
      rating: 3,
      insight: "Complete your birth profile for personalized insights",
      gradient: "from-blue-500 to-indigo-500"
    },
    { 
      area: "Health & Wellness", 
      icon: "Shield", 
      rating: 3,
      insight: "Complete your birth profile for personalized insights",
      gradient: "from-emerald-500 to-green-500"
    },
    { 
      area: "Spiritual Growth", 
      icon: "Sparkles", 
      rating: 3,
      insight: "Complete your birth profile for personalized insights",
      gradient: "from-purple-500 to-violet-500"
    }
  ];

  const displayLifeAreas = lifeAreaInfluences.length > 0 ? lifeAreaInfluences : defaultLifeAreas;

  return (
    <div className="min-h-screen bg-background text-foreground">
      <CosmicBackground />
      <Navigation />
      
      <main className="pt-20 pb-16 px-6">
        <div className="max-w-7xl mx-auto">
          {/* Error Message */}
          {error && (
            <motion.div
              initial={{ opacity: 0, y: -20 }}
              animate={{ opacity: 1, y: 0 }}
              className="mb-6 p-4 bg-red-500/10 border border-red-500/20 rounded-lg text-red-400"
            >
              {error}
            </motion.div>
          )}

          {/* Personalized Welcome */}
          <motion.section
            initial={{ opacity: 0, y: 30 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ duration: 0.8 }}
            className="mb-12"
          >
            <div className="relative p-8 rounded-2xl bg-gradient-mystical overflow-hidden">
              <div className="absolute inset-0 bg-black/20" />
              <div className="relative z-10">
                <div className="flex items-center gap-3 mb-4">
                  <Star className="h-8 w-8 text-white animate-twinkle" />
                  <h1 className="text-2xl md:text-3xl font-playfair font-bold text-white">
                    Welcome back, {user?.name}!
                  </h1>
                </div>
                
                {personalizedMessage && (
                  <div className="space-y-3">
                    <p className="text-white/90 text-lg leading-relaxed">
                      {personalizedMessage.message}
                    </p>
                    <div className="flex items-center gap-4">
                      <div className="flex items-center gap-2">
                        <div className="flex">
                          {[...Array(5)].map((_, i) => (
                            <Star
                              key={i}
                              className={`h-4 w-4 ${
                                i < personalizedMessage.intensity
                                  ? 'text-celestial-bright fill-current'
                                  : 'text-white/30'
                              }`}
                            />
                          ))}
                        </div>
                        <span className="text-white/80 text-sm">
                          Intensity: {personalizedMessage.intensity}/5
                        </span>
                      </div>
                      <div className="text-white/80 text-sm">
                        {personalizedMessage.transitInfluence}
                      </div>
                    </div>
                    <div className="grid grid-cols-2 md:grid-cols-4 gap-4 mt-4 pt-4 border-t border-white/20">
                      <div className="text-center">
                        <p className="text-white/60 text-xs">Dominant Planet</p>
                        <p className="text-white font-medium">{personalizedMessage.dominantPlanet}</p>
                      </div>
                      <div className="text-center">
                        <p className="text-white/60 text-xs">Lucky Color</p>
                        <p className="text-white font-medium">{personalizedMessage.luckyColor}</p>
                      </div>
                      <div className="text-center">
                        <p className="text-white/60 text-xs">Best Time</p>
                        <p className="text-white font-medium">{personalizedMessage.bestTimeOfDay}</p>
                      </div>
                      <div className="text-center">
                        <p className="text-white/60 text-xs">Moon Phase</p>
                        <p className="text-white font-medium">{personalizedMessage.moonPhase}</p>
                      </div>
                    </div>
                    {personalizedMessage.recommendation && (
                      <div className="mt-4 p-3 bg-white/10 rounded-lg">
                        <p className="text-white/90 text-sm">
                          <strong>Today's Guidance:</strong> {personalizedMessage.recommendation}
                        </p>
                      </div>
                    )}
                  </div>
                )}
              </div>
            </div>
          </motion.section>

          {/* User Stats */}
          {userStats && (
            <motion.section
              initial={{ opacity: 0, y: 20 }}
              animate={{ opacity: 1, y: 0 }}
              transition={{ duration: 0.6, delay: 0.2 }}
              className="mb-8"
            >
              <div className="grid grid-cols-2 md:grid-cols-4 gap-4">
                <Card className="bg-card/80 backdrop-blur-sm border-mystical-mid/20">
                  <CardContent className="p-4 text-center">
                    <Calendar className="h-6 w-6 text-celestial-mid mx-auto mb-2" />
                    <p className="text-2xl font-bold text-foreground">{userStats.chartsGenerated}</p>
                    <p className="text-xs text-muted-foreground">Charts Generated</p>
                  </CardContent>
                </Card>
                <Card className="bg-card/80 backdrop-blur-sm border-mystical-mid/20">
                  <CardContent className="p-4 text-center">
                    <Activity className="h-6 w-6 text-celestial-mid mx-auto mb-2" />
                    <p className="text-2xl font-bold text-foreground">{userStats.accuracyRate}%</p>
                    <p className="text-xs text-muted-foreground">Accuracy Rate</p>
                  </CardContent>
                </Card>
                <Card className="bg-card/80 backdrop-blur-sm border-mystical-mid/20">
                  <CardContent className="p-4 text-center">
                    <Sparkles className="h-6 w-6 text-celestial-mid mx-auto mb-2" />
                    <p className="text-2xl font-bold text-foreground">{userStats.cosmicEnergy}</p>
                    <p className="text-xs text-muted-foreground">Cosmic Energy</p>
                  </CardContent>
                </Card>
                <Card className="bg-card/80 backdrop-blur-sm border-mystical-mid/20">
                  <CardContent className="p-4 text-center">
                    <TrendingUp className="h-6 w-6 text-celestial-mid mx-auto mb-2" />
                    <p className="text-2xl font-bold text-foreground">{userStats.loginStreak}</p>
                    <p className="text-xs text-muted-foreground">Day Streak</p>
                  </CardContent>
                </Card>
              </div>
            </motion.section>
          )}

          {/* Planetary Ticker */}
          <PlanetaryTicker />

          {/* Main Content Grid */}
          <div className="grid lg:grid-cols-3 gap-8">
            {/* Left Column - Cosmic Weather */}
            <motion.div
              initial={{ opacity: 0, x: -30 }}
              animate={{ opacity: 1, x: 0 }}
              transition={{ duration: 0.8, delay: 0.3 }}
              className="lg:col-span-2"
            >
              <CosmicWeatherCard />
            </motion.div>

            {/* Right Column - Quick Actions */}
            <motion.div
              initial={{ opacity: 0, x: 30 }}
              animate={{ opacity: 1, x: 0 }}
              transition={{ duration: 0.8, delay: 0.4 }}
              className="space-y-6"
            >
              <Card className="bg-card/80 backdrop-blur-sm border-mystical-mid/20">
                <CardHeader>
                  <CardTitle className="flex items-center gap-2 text-foreground">
                    <Sparkles className="h-5 w-5 text-celestial-mid" />
                    Quick Actions
                  </CardTitle>
                  <CardDescription>
                    Explore your cosmic toolkit
                  </CardDescription>
                </CardHeader>
                <CardContent className="space-y-3">
                  {quickActions.map((action, index) => (
                    <Link key={index} to={action.href}>
                      <Button
                        variant="ghost"
                        className="w-full justify-start p-4 h-auto hover:bg-mystical-mid/10 border border-mystical-mid/20 hover:border-mystical-mid/40"
                      >
                        <div className="flex items-center gap-3 w-full">
                          <div className={`p-2 rounded-lg bg-${action.color}-mid/20 text-${action.color}-bright`}>
                            {action.icon}
                          </div>
                          <div className="text-left flex-1">
                            <div className="font-medium text-foreground">
                              {action.title}
                            </div>
                            <div className="text-sm text-muted-foreground">
                              {action.description}
                            </div>
                          </div>
                          <ArrowRight className="h-4 w-4 text-muted-foreground" />
                        </div>
                      </Button>
                    </Link>
                  ))}
                </CardContent>
              </Card>

              {/* Recent Charts */}
              <Card className="bg-card/80 backdrop-blur-sm border-mystical-mid/20">
                <CardHeader>
                  <CardTitle className="flex items-center gap-2 text-foreground">
                    <Moon className="h-5 w-5 text-celestial-mid" />
                    Recent Charts
                  </CardTitle>
                </CardHeader>
                <CardContent>
                  <div className="text-center py-8">
                    <Moon className="h-12 w-12 text-muted-foreground mx-auto mb-3 opacity-50" />
                    <p className="text-muted-foreground mb-4">
                      {userStats?.chartsGenerated === 0 ? 'No birth charts yet' : 'View your recent charts'}
                    </p>
                    <Link to="/birth-chart">
                      <Button size="sm" className="bg-mystical-mid hover:bg-mystical-bright text-white">
                        <Plus className="mr-2 h-4 w-4" />
                        {userStats?.chartsGenerated === 0 ? 'Create First Chart' : 'New Chart'}
                      </Button>
                    </Link>
                  </div>
                </CardContent>
              </Card>
            </motion.div>
          </div>

          {/* Bottom Section - Life Areas */}
          <motion.section
            initial={{ opacity: 0, y: 30 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ duration: 0.8, delay: 0.6 }}
            className="mt-12"
          >
            <h2 className="text-2xl font-playfair font-bold text-foreground mb-6">
              Life Area Influences
            </h2>
            <div className="grid md:grid-cols-2 lg:grid-cols-4 gap-6">
              {displayLifeAreas.map((area, index) => (
                <Card 
                  key={index} 
                  className="bg-card/50 backdrop-blur-sm border-mystical-mid/20 hover:border-mystical-mid/40 transition-colors cursor-pointer group"
                >
                  <CardContent className="p-6">
                    <div className="flex items-center gap-3 mb-3">
                      <div className="text-celestial-mid group-hover:scale-110 transition-transform">
                        {getLifeAreaIcon(area.icon)}
                      </div>
                      <h3 className="font-semibold text-foreground text-sm">
                        {area.area}
                      </h3>
                    </div>
                    <div className="flex items-center gap-2 mb-2">
                      <div className="flex">
                        {[...Array(5)].map((_, i) => (
                          <Star
                            key={i}
                            className={`h-3 w-3 ${
                              i < area.rating
                                ? 'text-celestial-bright fill-current'
                                : 'text-muted-foreground/30'
                            }`}
                          />
                        ))}
                      </div>
                      <span className="text-xs text-muted-foreground">
                        {area.rating}/5
                      </span>
                    </div>
                    <p className="text-xs text-muted-foreground">
                      {area.insight}
                    </p>
                  </CardContent>
                </Card>
              ))}
            </div>
          </motion.section>
        </div>
      </main>

      <Footer />
    </div>
  );
}
