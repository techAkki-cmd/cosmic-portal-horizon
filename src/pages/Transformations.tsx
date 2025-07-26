import React from 'react';
import { Navigation } from '@/components/Navigation';
import { Footer } from '@/components/Footer';
import { CosmicBackground } from '@/components/CosmicBackground';
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card';
import { Button } from '@/components/ui/button';
import { Progress } from '@/components/ui/progress';
import { 
  TrendingUp, 
  Target, 
  Brain,
  Heart,
  Compass,
  Lightbulb,
  ArrowRight,
  Star,
  Moon,
  Sparkles
} from 'lucide-react';
import { motion } from 'framer-motion';

export default function Transformations() {
  const transformationPaths = [
    {
      title: "Self-Discovery Journey",
      description: "Uncover your authentic self through cosmic guidance",
      icon: <Compass className="h-8 w-8" />,
      progress: 0,
      stages: ["Birth Chart Analysis", "Personality Insights", "Core Values", "Life Purpose"],
      color: "mystical"
    },
    {
      title: "Relationship Mastery",
      description: "Transform your connections with others",
      icon: <Heart className="h-8 w-8" />,
      progress: 0,
      stages: ["Communication Patterns", "Love Languages", "Compatibility Insights", "Healing"],
      color: "celestial"
    },
    {
      title: "Career Alignment",
      description: "Find your professional calling through the stars",
      icon: <Target className="h-8 w-8" />,
      progress: 0,
      stages: ["Vocational Analysis", "Strength Assessment", "Timing Guidance", "Success Strategy"],
      color: "cosmic"
    },
    {
      title: "Spiritual Awakening",
      description: "Deepen your connection to the universe",
      icon: <Lightbulb className="h-8 w-8" />,
      progress: 0,
      stages: ["Intuitive Development", "Meditation Guidance", "Energy Alignment", "Higher Purpose"],
      color: "mystical"
    }
  ];

  const currentTransits = [
    {
      planet: "Mercury",
      aspect: "Trine Jupiter",
      influence: "Expansion of knowledge and communication",
      duration: "Next 3 days",
      intensity: 4
    },
    {
      planet: "Venus",
      aspect: "Conjunct Mars",
      influence: "Passionate energy in relationships",
      duration: "This week",
      intensity: 5
    },
    {
      planet: "Moon",
      aspect: "Opposition Saturn",
      influence: "Emotional discipline and structure",
      duration: "Today",
      intensity: 3
    }
  ];

  return (
    <div className="min-h-screen bg-background text-foreground">
      <CosmicBackground />
      <Navigation />
      
      <main className="pt-20 pb-16 px-6">
        <div className="max-w-6xl mx-auto">
          <motion.div
            initial={{ opacity: 0, y: 30 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ duration: 0.8 }}
            className="text-center mb-12"
          >
            <div className="flex justify-center mb-6">
              <div className="p-4 rounded-full bg-gradient-cosmic">
                <TrendingUp className="h-12 w-12 text-white" />
              </div>
            </div>
            <h1 className="text-3xl md:text-4xl font-playfair font-bold text-foreground mb-4">
              Cosmic Transformations
            </h1>
            <p className="text-muted-foreground text-lg max-w-3xl mx-auto">
              Embark on guided journeys of personal growth using the wisdom of astrology. 
              Transform different areas of your life with cosmic insights and practical guidance.
            </p>
          </motion.div>

          {/* Current Transits */}
          <motion.section
            initial={{ opacity: 0, y: 30 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ duration: 0.8, delay: 0.2 }}
            className="mb-12"
          >
            <Card className="bg-gradient-mystical overflow-hidden">
              <div className="absolute inset-0 bg-black/20" />
              <CardContent className="relative z-10 p-8">
                <div className="flex items-center gap-3 mb-6">
                  <Moon className="h-8 w-8 text-white animate-pulse" />
                  <h2 className="text-2xl font-playfair font-bold text-white">
                    Current Transformative Energies
                  </h2>
                </div>
                
                <div className="grid md:grid-cols-3 gap-6">
                  {currentTransits.map((transit, index) => (
                    <div key={index} className="bg-white/10 backdrop-blur-sm rounded-lg p-4 border border-white/20">
                      <div className="flex items-center justify-between mb-3">
                        <h3 className="font-semibold text-white">
                          {transit.planet}
                        </h3>
                        <div className="flex">
                          {[...Array(5)].map((_, i) => (
                            <Star
                              key={i}
                              className={`h-3 w-3 ${
                                i < transit.intensity
                                  ? 'text-celestial-bright fill-current'
                                  : 'text-white/30'
                              }`}
                            />
                          ))}
                        </div>
                      </div>
                      <p className="text-white/90 text-sm mb-2">
                        {transit.aspect}
                      </p>
                      <p className="text-white/70 text-xs mb-3">
                        {transit.influence}
                      </p>
                      <div className="text-celestial-light text-xs">
                        {transit.duration}
                      </div>
                    </div>
                  ))}
                </div>
              </CardContent>
            </Card>
          </motion.section>

          {/* Transformation Paths */}
          <motion.section
            initial={{ opacity: 0, y: 30 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ duration: 0.8, delay: 0.4 }}
            className="mb-12"
          >
            <h2 className="text-2xl font-playfair font-bold text-foreground mb-8 text-center">
              Your Transformation Journeys
            </h2>
            
            <div className="grid lg:grid-cols-2 gap-8">
              {transformationPaths.map((path, index) => (
                <Card key={index} className="bg-card/80 backdrop-blur-sm border-mystical-mid/20 hover:border-mystical-mid/40 transition-all duration-300">
                  <CardHeader>
                    <div className="flex items-center gap-4 mb-4">
                      <div className={`w-12 h-12 rounded-lg bg-${path.color}-mid/20 flex items-center justify-center`}>
                        <div className={`text-${path.color}-bright`}>
                          {path.icon}
                        </div>
                      </div>
                      <div className="flex-1">
                        <CardTitle className="text-foreground">
                          {path.title}
                        </CardTitle>
                        <CardDescription>
                          {path.description}
                        </CardDescription>
                      </div>
                    </div>
                    
                    <div className="space-y-2">
                      <div className="flex justify-between text-sm">
                        <span className="text-muted-foreground">Progress</span>
                        <span className="text-foreground">{path.progress}%</span>
                      </div>
                      <Progress value={path.progress} className="h-2" />
                    </div>
                  </CardHeader>
                  
                  <CardContent>
                    <div className="space-y-3 mb-6">
                      {path.stages.map((stage, stageIndex) => (
                        <div key={stageIndex} className="flex items-center gap-3">
                          <div className={`w-2 h-2 rounded-full ${
                            stageIndex === 0 ? `bg-${path.color}-bright` : 'bg-muted-foreground/30'
                          }`} />
                          <span className={`text-sm ${
                            stageIndex === 0 ? 'text-foreground font-medium' : 'text-muted-foreground'
                          }`}>
                            {stage}
                          </span>
                        </div>
                      ))}
                    </div>
                    
                    <Button className={`w-full bg-${path.color}-mid hover:bg-${path.color}-bright text-white`}>
                      {path.progress === 0 ? 'Begin Journey' : 'Continue Journey'}
                      <ArrowRight className="ml-2 h-4 w-4" />
                    </Button>
                  </CardContent>
                </Card>
              ))}
            </div>
          </motion.section>

          {/* Transformation Tools */}
          <motion.section
            initial={{ opacity: 0, y: 30 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ duration: 0.8, delay: 0.6 }}
          >
            <h2 className="text-2xl font-playfair font-bold text-foreground mb-8 text-center">
              Transformation Tools
            </h2>
            
            <div className="grid md:grid-cols-3 gap-6">
              {[
                {
                  title: "Daily Reflection",
                  description: "Connect with cosmic energies through guided meditation",
                  icon: <Brain className="h-6 w-6" />,
                  action: "Start Session"
                },
                {
                  title: "Goal Alignment",
                  description: "Set intentions that align with planetary cycles",
                  icon: <Target className="h-6 w-6" />,
                  action: "Set Goals"
                },
                {
                  title: "Energy Tracking",
                  description: "Monitor how cosmic influences affect your daily life",
                  icon: <Sparkles className="h-6 w-6" />,
                  action: "Track Energy"
                }
              ].map((tool, index) => (
                <Card key={index} className="bg-card/50 backdrop-blur-sm border-mystical-mid/20 hover:border-mystical-mid/40 transition-colors">
                  <CardContent className="p-6 text-center">
                    <div className="w-12 h-12 bg-celestial-mid/20 rounded-lg flex items-center justify-center mx-auto mb-4">
                      <div className="text-celestial-bright">
                        {tool.icon}
                      </div>
                    </div>
                    <h3 className="font-semibold text-foreground mb-2">
                      {tool.title}
                    </h3>
                    <p className="text-muted-foreground text-sm mb-4">
                      {tool.description}
                    </p>
                    <Button size="sm" variant="outline" className="border-mystical-mid/30 text-mystical-bright hover:bg-mystical-mid/10">
                      {tool.action}
                    </Button>
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