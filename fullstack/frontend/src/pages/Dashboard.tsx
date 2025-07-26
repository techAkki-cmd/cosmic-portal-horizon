// src/pages/Dashboard.tsx
import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { Button } from '@/components/ui/button';
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card';
import { Badge } from '@/components/ui/badge';
import { Progress } from '@/components/ui/progress';
import { CosmicBackground } from '@/components/CosmicBackground';
import { CosmicWeatherCard } from '@/components/CosmicWeatherCard';
import { PlanetaryTicker } from '@/components/PlanetaryTicker';
import { Navigation } from '@/components/Navigation';
import { Footer } from '@/components/Footer';
import { useAuth } from '@/contexts/AuthContext';
import { astrologyService, PersonalizedMessage, LifeAreaInfluence, UserStats } from '@/services/astrologyService';
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
  Zap,
  Eye,
  Clock,
  Award,
  BarChart3,
  Compass,
  Crown
} from 'lucide-react';
import { motion, useScroll, useTransform } from 'framer-motion';

export default function Dashboard() {
  const { user } = useAuth();
  const [personalizedMessage, setPersonalizedMessage] = useState<PersonalizedMessage | null>(null);
  const [lifeAreas, setLifeAreas] = useState<LifeAreaInfluence[]>([]);
  const [userStats, setUserStats] = useState<UserStats | null>(null);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const { scrollY } = useScroll();
  const backgroundY = useTransform(scrollY, [0, 500], [0, 150]);

  useEffect(() => {
    const fetchDashboardData = async () => {
      if (!user) return;
      
      setIsLoading(true);
      setError(null);
      
      try {
        // Fetch all personalized data
        const [messageData, lifeAreasData, statsData] = await Promise.all([
          astrologyService.getPersonalizedMessage(),
          astrologyService.getLifeAreaInfluences(),
          astrologyService.getUserStats()
        ]);
        
        setPersonalizedMessage(messageData);
        setLifeAreas(lifeAreasData);
        setUserStats(statsData);
        
      } catch (error) {
        console.error('Failed to fetch dashboard data:', error);
        setError('Failed to load personalized content');
        
        // Set fallback data
        setPersonalizedMessage({
          message: `Welcome back, ${user?.firstName || user?.username}! Complete your birth profile for personalized insights.`,
          transitInfluence: "Complete birth data needed for accurate analysis",
          recommendation: "Add your birth information for full personalization",
          intensity: 3
        });
        
        setLifeAreas([
          { title: "Love & Relationships", rating: 3, insight: "Complete profile for insights", icon: "Heart", gradient: "from-pink-500 to-rose-500" },
          { title: "Career & Success", rating: 3, insight: "Complete profile for insights", icon: "Briefcase", gradient: "from-blue-500 to-indigo-500" },
          { title: "Health & Wellness", rating: 3, insight: "Complete profile for insights", icon: "Shield", gradient: "from-emerald-500 to-green-500" },
          { title: "Spiritual Growth", rating: 3, insight: "Complete profile for insights", icon: "Sparkles", gradient: "from-purple-500 to-violet-500" }
        ]);
        
        setUserStats({
          chartsCreated: 0,
          accuracyRate: 0,
          cosmicEnergy: "Low",
          streakDays: 0
        });
      } finally {
        setIsLoading(false);
      }
    };

    fetchDashboardData();
  }, [user]);

  const quickActions = [
    {
      title: "Generate Birth Chart",
      description: "Create detailed natal chart analysis",
      href: "/birth-chart",
      icon: <Moon className="h-5 w-5" />,
      color: "from-purple-500 to-pink-500",
      bgColor: "bg-purple-500/10",
      borderColor: "border-purple-500/20",
      isNew: false
    },
    {
      title: "Advanced Tools",
      description: "Compatibility, transits, and forecasts",
      href: "/advanced-tools",
      icon: <Sparkles className="h-5 w-5" />,
      color: "from-blue-500 to-cyan-500",
      bgColor: "bg-blue-500/10",
      borderColor: "border-blue-500/20",
      isNew: true
    },
    {
      title: "Cosmic Transformations",
      description: "Personal growth insights",
      href: "/transformations",
      icon: <TrendingUp className="h-5 w-5" />,
      color: "from-emerald-500 to-teal-500",
      bgColor: "bg-emerald-500/10",
      borderColor: "border-emerald-500/20",
      isNew: false
    },
    {
      title: "Daily Insights",
      description: "Personalized cosmic guidance",
      href: "/daily-insights",
      icon: <Eye className="h-5 w-5" />,
      color: "from-orange-500 to-red-500",
      bgColor: "bg-orange-500/10",
      borderColor: "border-orange-500/20",
      isNew: false
    }
  ];

  const stats = [
    { 
      label: "Charts Created", 
      value: userStats?.chartsCreated?.toString() || "0", 
      icon: <BarChart3 className="h-4 w-4" />, 
      change: userStats?.chartsCreated ? "+2 this week" : "Get started!" 
    },
    { 
      label: "Accuracy Rate", 
      value: `${userStats?.accuracyRate || 0}%`, 
      icon: <Award className="h-4 w-4" />, 
      change: userStats?.accuracyRate ? "+3% this month" : "Build experience" 
    },
    { 
      label: "Cosmic Energy", 
      value: userStats?.cosmicEnergy || "Low", 
      icon: <Zap className="h-4 w-4" />, 
      change: userStats?.cosmicEnergy === "High" ? "Peak today" : "Growing stronger" 
    },
    { 
      label: "Streak Days", 
      value: userStats?.streakDays?.toString() || "0", 
      icon: <Crown className="h-4 w-4" />, 
      change: userStats?.streakDays ? "Keep it up!" : "Start your journey" 
    }
  ];

  if (isLoading) {
    return (
      <div className="min-h-screen bg-background text-foreground relative overflow-hidden">
        <CosmicBackground />
        <Navigation />
        <div className="flex items-center justify-center min-h-screen">
          <div className="flex flex-col items-center space-y-4">
            <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-mystical-mid"></div>
            <p className="text-muted-foreground">Loading your cosmic insights...</p>
          </div>
        </div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-background text-foreground relative overflow-hidden">
      <CosmicBackground />
      <Navigation />
      
      <main className="relative z-10">
        {/* Hero Section with Enhanced Welcome */}
        <motion.section 
          style={{ y: backgroundY }}
          className="pt-24 pb-8 px-4 sm:px-6 lg:px-8"
        >
          <div className="max-w-7xl mx-auto">
            <motion.div
              initial={{ opacity: 0, y: 40 }}
              animate={{ opacity: 1, y: 0 }}
              transition={{ duration: 1, ease: "easeOut" }}
              className="relative"
            >
              {/* Enhanced Personalized Welcome */}
              <div className="relative p-8 sm:p-12 rounded-3xl bg-gradient-to-br from-purple-900/80 via-blue-900/60 to-indigo-900/80 backdrop-blur-xl border border-white/10 overflow-hidden mb-8">
                {/* Animated Background Elements */}
                <div className="absolute inset-0">
                  <div className="absolute top-4 right-4 w-32 h-32 bg-gradient-to-br from-white/5 to-transparent rounded-full blur-xl" />
                  <div className="absolute bottom-4 left-4 w-24 h-24 bg-gradient-to-br from-purple-500/10 to-transparent rounded-full blur-lg" />
                  <div className="absolute top-1/2 left-1/2 transform -translate-x-1/2 -translate-y-1/2 w-96 h-96 bg-gradient-radial from-white/5 to-transparent rounded-full blur-3xl" />
                </div>
                
                <div className="relative z-10">
                  <div className="flex flex-col sm:flex-row sm:items-start sm:justify-between gap-6">
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
                            Welcome back, {user?.firstName || 'Cosmic Explorer'}!
                          </h1>
                          <Badge variant="secondary" className="bg-white/10 text-white/80 border-white/20">
                            <Clock className="w-3 h-3 mr-1" />
                            {new Date().toLocaleDateString('en-US', { weekday: 'long', month: 'long', day: 'numeric' })}
                          </Badge>
                        </div>
                      </div>
                      
                      {personalizedMessage && (
                        <motion.div 
                          initial={{ opacity: 0 }}
                          animate={{ opacity: 1 }}
                          transition={{ delay: 0.5 }}
                          className="space-y-4"
                        >
                          <p className="text-xl text-white/90 leading-relaxed font-light">
                            {personalizedMessage.message}
                          </p>
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
                                        i < personalizedMessage.intensity
                                          ? 'text-yellow-400 fill-current'
                                          : 'text-white/30'
                                      }`}
                                    />
                                  </motion.div>
                                ))}
                              </div>
                              <span className="text-white/70 text-sm font-medium">
                                Cosmic Intensity: {personalizedMessage.intensity}/5
                              </span>
                            </div>
                            <div className="px-4 py-2 rounded-full bg-white/10 backdrop-blur-sm border border-white/20">
                              <span className="text-white/80 text-sm">
                                {personalizedMessage.transitInfluence}
                              </span>
                            </div>
                          </div>
                          {personalizedMessage.recommendation && (
                            <div className="mt-4 p-4 rounded-xl bg-white/5 backdrop-blur-sm border border-white/10">
                              <p className="text-white/80 text-sm">
                                <strong>Today's Guidance:</strong> {personalizedMessage.recommendation}
                              </p>
                            </div>
                          )}
                        </motion.div>
                      )}
                    </div>

                    {/* User Stats Panel */}
                    <motion.div 
                      initial={{ opacity: 0, x: 20 }}
                      animate={{ opacity: 1, x: 0 }}
                      transition={{ delay: 0.8 }}
                      className="grid grid-cols-2 gap-3 sm:w-80"
                    >
                      {stats.map((stat, index) => (
                        <div key={index} className="p-4 rounded-xl bg-white/5 backdrop-blur-sm border border-white/10">
                          <div className="flex items-center gap-2 mb-1">
                            <div className="text-white/70">{stat.icon}</div>
                            <span className="text-white/70 text-xs font-medium">{stat.label}</span>
                          </div>
                          <div className="text-white text-lg font-bold">{stat.value}</div>
                          <div className="text-white/50 text-xs">{stat.change}</div>
                        </div>
                      ))}
                    </motion.div>
                  </div>
                </div>
              </div>

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

        {/* Main Dashboard Content */}
        <section className="px-4 sm:px-6 lg:px-8 pb-16">
          <div className="max-w-7xl mx-auto">
            
            {/* Enhanced Main Grid Layout */}
            <div className="grid grid-cols-1 xl:grid-cols-12 gap-8 mb-12">
              
              {/* Left Column - Cosmic Weather (Enhanced) */}
              <motion.div
                initial={{ opacity: 0, x: -30 }}
                animate={{ opacity: 1, x: 0 }}
                transition={{ duration: 0.8, delay: 0.2 }}
                className="xl:col-span-8 space-y-6"
              >
                <CosmicWeatherCard />
                
                {/* Additional Insights Card */}
                <Card className="bg-card/60 backdrop-blur-xl border-mystical-mid/20 overflow-hidden">
                  <CardHeader className="pb-4">
                    <CardTitle className="flex items-center gap-2 text-foreground">
                      <Compass className="h-5 w-5 text-celestial-mid" />
                      Today's Cosmic Guidance
                    </CardTitle>
                  </CardHeader>
                  <CardContent>
                    <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
                      {personalizedMessage && [
                        { 
                          title: "Dominant Planet", 
                          value: personalizedMessage.dominantPlanet || "Sun",
                          color: "purple" 
                        },
                        { 
                          title: "Lucky Color", 
                          value: personalizedMessage.luckyColor || "White",
                          color: "blue" 
                        },
                        { 
                          title: "Best Time", 
                          value: personalizedMessage.bestTimeOfDay || "Morning",
                          color: "emerald" 
                        }
                      ].map((item, index) => (
                        <div key={index} className={`p-4 rounded-xl bg-gradient-to-br from-${item.color}-500/10 to-${item.color}-500/10 border border-${item.color}-500/20`}>
                          <h4 className="font-semibold text-foreground mb-1">{item.title}</h4>
                          <p className="text-sm text-muted-foreground">{item.value}</p>
                        </div>
                      ))}
                    </div>
                  </CardContent>
                </Card>
              </motion.div>

              {/* Right Column - Enhanced Sidebar */}
              <motion.div
                initial={{ opacity: 0, x: 30 }}
                animate={{ opacity: 1, x: 0 }}
                transition={{ duration: 0.8, delay: 0.4 }}
                className="xl:col-span-4 space-y-6"
              >
                
                {/* Enhanced Quick Actions */}
                <Card className="bg-card/60 backdrop-blur-xl border-mystical-mid/20 overflow-hidden">
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
                      <motion.div
                        key={index}
                        initial={{ opacity: 0, y: 20 }}
                        animate={{ opacity: 1, y: 0 }}
                        transition={{ delay: 0.6 + index * 0.1 }}
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
                  </CardContent>
                </Card>

                {/* Enhanced Recent Charts */}
                <Card className="bg-card/60 backdrop-blur-xl border-mystical-mid/20">
                  <CardHeader>
                    <CardTitle className="flex items-center gap-2 text-foreground">
                      <Moon className="h-5 w-5 text-celestial-mid" />
                      Recent Charts
                    </CardTitle>
                  </CardHeader>
                  <CardContent>
                    <div className="text-center py-6">
                      <motion.div
                        animate={{ 
                          scale: [1, 1.1, 1],
                          rotate: [0, 5, -5, 0]
                        }}
                        transition={{ 
                          duration: 4,
                          repeat: Infinity,
                          ease: "easeInOut"
                        }}
                        className="inline-block"
                      >
                        <Moon className="h-16 w-16 text-muted-foreground mx-auto mb-4 opacity-50" />
                      </motion.div>
                      <h3 className="font-semibold text-foreground mb-2">Begin Your Journey</h3>
                      <p className="text-muted-foreground mb-4 text-sm">
                        Create your first birth chart to unlock personalized cosmic insights
                      </p>
                      <Link to="/birth-chart">
                        <Button className="bg-gradient-to-r from-purple-500 to-pink-500 hover:from-purple-600 hover:to-pink-600 text-white border-0 shadow-lg shadow-purple-500/25">
                          <Plus className="mr-2 h-4 w-4" />
                          Create First Chart
                        </Button>
                      </Link>
                    </div>
                  </CardContent>
                </Card>

                {/* Mini Cosmic Energy Meter */}
                <Card className="bg-card/60 backdrop-blur-xl border-mystical-mid/20">
                  <CardHeader className="pb-4">
                    <CardTitle className="flex items-center gap-2 text-foreground text-base">
                      <Zap className="h-4 w-4 text-yellow-400" />
                      Cosmic Energy Level
                    </CardTitle>
                  </CardHeader>
                  <CardContent>
                    <div className="space-y-3">
                      <div className="flex justify-between text-sm">
                        <span className="text-muted-foreground">Current Level</span>
                        <span className="font-semibold text-foreground">
                          {userStats?.cosmicEnergy === "High" ? "87%" : 
                           userStats?.cosmicEnergy === "Medium" ? "65%" : "43%"}
                        </span>
                      </div>
                      <Progress 
                        value={
                          userStats?.cosmicEnergy === "High" ? 87 : 
                          userStats?.cosmicEnergy === "Medium" ? 65 : 43
                        } 
                        className="h-2" 
                      />
                      <p className="text-xs text-muted-foreground">
                        {personalizedMessage?.moonPhase && `${personalizedMessage.moonPhase} energy influences`}
                      </p>
                    </div>
                  </CardContent>
                </Card>
              </motion.div>
            </div>

            {/* Enhanced Life Areas Section */}
            <motion.section
              initial={{ opacity: 0, y: 40 }}
              animate={{ opacity: 1, y: 0 }}
              transition={{ duration: 0.8, delay: 0.8 }}
            >
              <div className="flex items-center justify-between mb-8">
                <h2 className="text-3xl font-playfair font-bold text-foreground">
                  Life Area Influences
                </h2>
                <Badge variant="outline" className="text-muted-foreground">
                  Updated {new Date().toLocaleTimeString('en-US', { hour: '2-digit', minute: '2-digit' })}
                </Badge>
              </div>
              
              <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-6">
                {lifeAreas.map((area, index) => (
                  <motion.div
                    key={index}
                    initial={{ opacity: 0, y: 20, scale: 0.95 }}
                    animate={{ opacity: 1, y: 0, scale: 1 }}
                    transition={{ delay: 1 + index * 0.1 }}
                    whileHover={{ scale: 1.02, y: -5 }}
                    className="group"
                  >
                    <Card className={`bg-gradient-to-br ${area.gradient}/10 backdrop-blur-xl border-white/10 hover:border-white/20 transition-all duration-300 cursor-pointer h-full relative overflow-hidden`}>
                      {/* Hover Effect Overlay */}
                      <div className={`absolute inset-0 bg-gradient-to-br ${area.gradient} opacity-0 group-hover:opacity-5 transition-opacity duration-300`} />
                      
                      <CardContent className="p-6 relative z-10">
                        <div className="flex items-center gap-3 mb-4">
                          <div className={`p-2.5 rounded-xl bg-gradient-to-br ${area.gradient} text-white shadow-lg`}>
                            {area.icon === "Heart" && <Heart className="h-6 w-6" />}
                            {area.icon === "Briefcase" && <Briefcase className="h-6 w-6" />}
                            {area.icon === "Shield" && <Shield className="h-6 w-6" />}
                            {area.icon === "Sparkles" && <Sparkles className="h-6 w-6" />}
                          </div>
                          <h3 className="font-semibold text-foreground">
                            {area.title}
                          </h3>
                        </div>
                        
                        <div className="flex items-center gap-3 mb-3">
                          <div className="flex">
                            {[...Array(5)].map((_, i) => (
                              <motion.div
                                key={i}
                                initial={{ scale: 0 }}
                                animate={{ scale: 1 }}
                                transition={{ delay: 1.2 + index * 0.1 + i * 0.05 }}
                              >
                                <Star
                                  className={`h-4 w-4 ${
                                    i < area.rating
                                      ? `text-transparent bg-gradient-to-r ${area.gradient} bg-clip-text fill-current`
                                      : 'text-muted-foreground/30'
                                  }`}
                                />
                              </motion.div>
                            ))}
                          </div>
                          <span className="text-sm font-medium text-foreground">
                            {area.rating}/5
                          </span>
                        </div>
                        
                        <p className="text-sm text-muted-foreground leading-relaxed">
                          {area.insight}
                        </p>
                        
                        {/* Progress Bar */}
                        <div className="mt-4 space-y-2">
                          <div className="flex justify-between text-xs text-muted-foreground">
                            <span>Influence Level</span>
                            <span>{area.rating * 20}%</span>
                          </div>
                          <Progress value={area.rating * 20} className="h-1.5" />
                        </div>
                      </CardContent>
                    </Card>
                  </motion.div>
                ))}
              </div>
            </motion.section>
          </div>
        </section>
      </main>

      <Footer />
    </div>
  );
}
