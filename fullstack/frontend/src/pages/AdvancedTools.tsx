import React from 'react';
import { Navigation } from '@/components/Navigation';
import { Footer } from '@/components/Footer';
import { CosmicBackground } from '@/components/CosmicBackground';
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card';
import { Button } from '@/components/ui/button';
import { 
  Sparkles, 
  Users, 
  TrendingUp, 
  Calendar, 
  Heart,
  Clock,
  Target,
  Zap,
  ArrowRight
} from 'lucide-react';
import { motion } from 'framer-motion';

export default function AdvancedTools() {
  const tools = [
    {
      title: "Compatibility Analysis",
      description: "Compare birth charts to understand relationship dynamics",
      icon: <Heart className="h-8 w-8" />,
      features: ["Synastry charts", "Composite charts", "Love compatibility"],
      color: "mystical",
      comingSoon: false
    },
    {
      title: "Transit Forecasts",
      description: "See how planetary movements affect your personal chart",
      icon: <TrendingUp className="h-8 w-8" />,
      features: ["Daily transits", "Monthly forecasts", "Yearly overviews"],
      color: "celestial",
      comingSoon: false
    },
    {
      title: "Solar Return Charts",
      description: "Annual birthday charts predicting the year ahead",
      icon: <Calendar className="h-8 w-8" />,
      features: ["Birthday analysis", "Yearly themes", "Major life events"],
      color: "cosmic",
      comingSoon: true
    },
    {
      title: "Electional Astrology",
      description: "Find the best times for important decisions and events",
      icon: <Clock className="h-8 w-8" />,
      features: ["Wedding dates", "Business launches", "Medical procedures"],
      color: "mystical",
      comingSoon: true
    },
    {
      title: "Career Guidance",
      description: "Discover your vocational strengths and ideal career paths",
      icon: <Target className="h-8 w-8" />,
      features: ["Midheaven analysis", "10th house themes", "Professional timing"],
      color: "celestial",
      comingSoon: true
    },
    {
      title: "Horary Questions",
      description: "Get specific answers to pressing questions through charts",
      icon: <Zap className="h-8 w-8" />,
      features: ["Yes/no answers", "Lost objects", "Decision making"],
      color: "cosmic",
      comingSoon: true
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
              <div className="p-4 rounded-full bg-gradient-celestial">
                <Sparkles className="h-12 w-12 text-white" />
              </div>
            </div>
            <h1 className="text-3xl md:text-4xl font-playfair font-bold text-foreground mb-4">
              Advanced Cosmic Tools
            </h1>
            <p className="text-muted-foreground text-lg max-w-3xl mx-auto">
              Dive deeper into the cosmic influences with our professional-grade astrology tools. 
              Unlock advanced insights for relationships, timing, and life guidance.
            </p>
          </motion.div>

          <div className="grid md:grid-cols-2 lg:grid-cols-3 gap-8">
            {tools.map((tool, index) => (
              <motion.div
                key={index}
                initial={{ opacity: 0, y: 30 }}
                animate={{ opacity: 1, y: 0 }}
                transition={{ duration: 0.8, delay: index * 0.1 }}
              >
                <Card className="bg-card/80 backdrop-blur-sm border-mystical-mid/20 hover:border-mystical-mid/40 transition-all duration-300 h-full group">
                  <CardHeader>
                    <div className={`w-16 h-16 rounded-lg bg-${tool.color}-mid/20 flex items-center justify-center mb-4 group-hover:scale-110 transition-transform`}>
                      <div className={`text-${tool.color}-bright`}>
                        {tool.icon}
                      </div>
                    </div>
                    <CardTitle className="text-foreground text-xl">
                      {tool.title}
                      {tool.comingSoon && (
                        <span className="ml-2 text-xs bg-celestial-mid/20 text-celestial-bright px-2 py-1 rounded-full">
                          Coming Soon
                        </span>
                      )}
                    </CardTitle>
                    <CardDescription className="text-muted-foreground">
                      {tool.description}
                    </CardDescription>
                  </CardHeader>
                  
                  <CardContent className="flex-1 flex flex-col justify-between">
                    <div className="space-y-3 mb-6">
                      {tool.features.map((feature, featureIndex) => (
                        <div key={featureIndex} className="flex items-center gap-2">
                          <div className={`w-1.5 h-1.5 rounded-full bg-${tool.color}-bright`} />
                          <span className="text-sm text-muted-foreground">
                            {feature}
                          </span>
                        </div>
                      ))}
                    </div>
                    
                    <Button 
                      className={`w-full ${
                        tool.comingSoon 
                          ? 'bg-muted text-muted-foreground cursor-not-allowed' 
                          : `bg-${tool.color}-mid hover:bg-${tool.color}-bright text-white`
                      }`}
                      disabled={tool.comingSoon}
                    >
                      {tool.comingSoon ? (
                        <>
                          <Clock className="mr-2 h-4 w-4" />
                          Coming Soon
                        </>
                      ) : (
                        <>
                          Explore Tool
                          <ArrowRight className="ml-2 h-4 w-4" />
                        </>
                      )}
                    </Button>
                  </CardContent>
                </Card>
              </motion.div>
            ))}
          </div>

          {/* Featured Tool Section */}
          <motion.section
            initial={{ opacity: 0, y: 30 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ duration: 0.8, delay: 0.6 }}
            className="mt-16"
          >
            <Card className="bg-gradient-mystical overflow-hidden">
              <div className="absolute inset-0 bg-black/20" />
              <CardContent className="relative z-10 p-8 md:p-12">
                <div className="grid md:grid-cols-2 gap-8 items-center">
                  <div>
                    <div className="flex items-center gap-3 mb-4">
                      <Users className="h-8 w-8 text-white" />
                      <h2 className="text-2xl md:text-3xl font-playfair font-bold text-white">
                        Relationship Compatibility
                      </h2>
                    </div>
                    <p className="text-white/90 text-lg mb-6 leading-relaxed">
                      Discover the cosmic chemistry between you and your partner. Our advanced 
                      synastry analysis reveals the strengths, challenges, and potential of your relationship.
                    </p>
                    <Button 
                      size="lg" 
                      className="bg-white text-space-void hover:bg-white/90"
                    >
                      <Heart className="mr-2 h-5 w-5" />
                      Analyze Compatibility
                    </Button>
                  </div>
                  
                  <div className="relative">
                    <div className="aspect-square rounded-2xl bg-white/10 backdrop-blur-sm border border-white/20 flex items-center justify-center">
                      <div className="text-center">
                        <Users className="h-16 w-16 text-white/60 mx-auto mb-4" />
                        <p className="text-white/80 text-sm">
                          Interactive Synastry Chart
                        </p>
                      </div>
                    </div>
                  </div>
                </div>
              </CardContent>
            </Card>
          </motion.section>
        </div>
      </main>

      <Footer />
    </div>
  );
}