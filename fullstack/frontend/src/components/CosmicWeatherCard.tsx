import { useState } from 'react';
import { Card } from './ui/card';
import { Badge } from './ui/badge';
import { Button } from './ui/button';
import { Star, TrendingUp, Heart, Briefcase, Zap, ChevronDown, ChevronUp } from 'lucide-react';
import { motion, AnimatePresence } from 'framer-motion';

interface CosmicInfluence {
  area: string;
  rating: number;
  description: string;
  color: string;
  icon: React.ReactNode;
}

export function CosmicWeatherCard() {
  const [isExpanded, setIsExpanded] = useState(false);
  
  const influences: CosmicInfluence[] = [
    {
      area: "Love & Relationships",
      rating: 4,
      description: "Venus in Aquarius brings innovative energy to partnerships. Perfect time for deep conversations.",
      color: "mystical-bright",
      icon: <Heart className="h-4 w-4" />
    },
    {
      area: "Career & Finance",
      rating: 5,
      description: "Jupiter's influence amplifies opportunities. A powerful day for negotiations and new ventures.",
      color: "celestial-mid",
      icon: <Briefcase className="h-4 w-4" />
    },
    {
      area: "Health & Energy",
      rating: 3,
      description: "Mars square Mercury suggests taking it slow. Focus on mindful practices and rest.",
      color: "cosmic-bright",
      icon: <Zap className="h-4 w-4" />
    },
    {
      area: "Personal Growth",
      rating: 5,
      description: "Transformative Pluto aspects support profound inner work and breakthrough moments.",
      color: "mystical-mid",
      icon: <TrendingUp className="h-4 w-4" />
    }
  ];

  const overallRating = Math.round(influences.reduce((sum, inf) => sum + inf.rating, 0) / influences.length);

  const renderStars = (rating: number) => {
    return Array.from({ length: 5 }, (_, i) => (
      <Star
        key={i}
        className={`h-4 w-4 ${
          i < rating ? "fill-celestial-mid text-celestial-mid" : "text-muted-foreground"
        }`}
      />
    ));
  };

  return (
    <Card className="bg-space-dark/80 backdrop-blur-sm border-mystical-mid/20 p-6 shadow-cosmic">
      <div className="flex items-center justify-between mb-4">
        <div className="flex items-center gap-3">
          <div className="p-2 rounded-full bg-gradient-mystical">
            <Star className="h-6 w-6 text-white" />
          </div>
          <div>
            <h3 className="text-xl font-playfair font-semibold text-foreground">
              Today's Cosmic Weather
            </h3>
            <p className="text-sm text-muted-foreground">
              {new Date().toLocaleDateString('en-US', { 
                weekday: 'long', 
                year: 'numeric', 
                month: 'long', 
                day: 'numeric' 
              })}
            </p>
          </div>
        </div>
        
        <div className="text-center">
          <div className="flex items-center gap-1 mb-1">
            {renderStars(overallRating)}
          </div>
          <Badge variant="secondary" className="bg-celestial-mid/20 text-celestial-mid border-celestial-mid/30">
            {overallRating}/5 Cosmic Energy
          </Badge>
        </div>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 gap-4 mb-4">
        {influences.slice(0, 2).map((influence, index) => (
          <motion.div
            key={influence.area}
            initial={{ opacity: 0, y: 20 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ delay: index * 0.1 }}
            className="bg-card/30 rounded-lg p-4 border border-mystical-mid/10"
          >
            <div className="flex items-center justify-between mb-2">
              <div className="flex items-center gap-2">
                <div className={`text-${influence.color}`}>
                  {influence.icon}
                </div>
                <span className="font-medium text-foreground">{influence.area}</span>
              </div>
              <div className="flex items-center gap-1">
                {renderStars(influence.rating)}
              </div>
            </div>
            <p className="text-sm text-muted-foreground">{influence.description}</p>
          </motion.div>
        ))}
      </div>

      <AnimatePresence>
        {isExpanded && (
          <motion.div
            initial={{ opacity: 0, height: 0 }}
            animate={{ opacity: 1, height: "auto" }}
            exit={{ opacity: 0, height: 0 }}
            className="grid grid-cols-1 md:grid-cols-2 gap-4 mb-4"
          >
            {influences.slice(2).map((influence, index) => (
              <motion.div
                key={influence.area}
                initial={{ opacity: 0, y: 20 }}
                animate={{ opacity: 1, y: 0 }}
                transition={{ delay: index * 0.1 }}
                className="bg-card/30 rounded-lg p-4 border border-mystical-mid/10"
              >
                <div className="flex items-center justify-between mb-2">
                  <div className="flex items-center gap-2">
                    <div className={`text-${influence.color}`}>
                      {influence.icon}
                    </div>
                    <span className="font-medium text-foreground">{influence.area}</span>
                  </div>
                  <div className="flex items-center gap-1">
                    {renderStars(influence.rating)}
                  </div>
                </div>
                <p className="text-sm text-muted-foreground">{influence.description}</p>
              </motion.div>
            ))}
          </motion.div>
        )}
      </AnimatePresence>

      <Button
        onClick={() => setIsExpanded(!isExpanded)}
        variant="ghost"
        className="w-full text-mystical-bright hover:text-mystical-mid hover:bg-mystical-mid/10"
      >
        {isExpanded ? (
          <>
            <ChevronUp className="h-4 w-4 mr-2" />
            Show Less
          </>
        ) : (
          <>
            <ChevronDown className="h-4 w-4 mr-2" />
            View All Influences
          </>
        )}
      </Button>
    </Card>
  );
}