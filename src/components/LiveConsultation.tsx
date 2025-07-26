import { useState, useEffect } from 'react';
import { Card } from './ui/card';
import { Button } from './ui/button';
import { Badge } from './ui/badge';
import { Avatar, AvatarImage, AvatarFallback } from './ui/avatar';
import { Calendar, Clock, Star, Video, MessageCircle, Sparkles } from 'lucide-react';
import { motion } from 'framer-motion';
import astrologerPortrait from '../assets/astrologer-portrait.jpg';

export function LiveConsultation() {
  const [isOnline, setIsOnline] = useState(true);
  const [nextAvailable, setNextAvailable] = useState("2:30 PM Today");
  const [pulseKey, setPulseKey] = useState(0);

  useEffect(() => {
    const interval = setInterval(() => {
      setPulseKey(prev => prev + 1);
    }, 2000);

    return () => clearInterval(interval);
  }, []);

  const consultationTypes = [
    { type: "Quick Insight", duration: "15 min", price: "$45", popular: false },
    { type: "Full Reading", duration: "60 min", price: "$180", popular: true },
    { type: "Relationship", duration: "45 min", price: "$150", popular: false },
  ];

  return (
    <Card className="bg-space-dark/80 backdrop-blur-sm border-mystical-mid/20 p-6 shadow-cosmic relative overflow-hidden">
      {/* Animated background elements */}
      <div className="absolute top-0 right-0 w-32 h-32 bg-gradient-mystical opacity-10 rounded-full blur-3xl animate-pulse" />
      <div className="absolute bottom-0 left-0 w-24 h-24 bg-gradient-celestial opacity-10 rounded-full blur-2xl animate-float" />

      <div className="relative z-10">
        <div className="flex items-start gap-4 mb-6">
          <div className="relative">
            <Avatar className="w-16 h-16 border-2 border-celestial-mid/50">
              <AvatarImage src={astrologerPortrait} alt="Astrologer" />
              <AvatarFallback className="bg-mystical-mid text-white">AS</AvatarFallback>
            </Avatar>
            
            {/* Online status indicator */}
            <motion.div
              key={pulseKey}
              initial={{ scale: 0.8, opacity: 0.7 }}
              animate={{ scale: 1.2, opacity: 1 }}
              transition={{ duration: 0.8, ease: "easeOut" }}
              className={`absolute -top-1 -right-1 w-5 h-5 rounded-full border-2 border-space-dark ${
                isOnline ? 'bg-green-500' : 'bg-gray-500'
              }`}
            />
          </div>

          <div className="flex-1">
            <div className="flex items-center gap-2 mb-1">
              <h3 className="text-xl font-playfair font-semibold text-foreground">
                Celestial Sage Aurora
              </h3>
              <Badge variant="secondary" className="bg-celestial-mid/20 text-celestial-mid border-celestial-mid/30">
                <Sparkles className="h-3 w-3 mr-1" />
                Master
              </Badge>
            </div>
            
            <div className="flex items-center gap-4 mb-2">
              <div className="flex items-center gap-1">
                {Array.from({ length: 5 }, (_, i) => (
                  <Star key={i} className="h-4 w-4 fill-celestial-mid text-celestial-mid" />
                ))}
                <span className="text-sm text-muted-foreground ml-1">(847 reviews)</span>
              </div>
            </div>

            <p className="text-sm text-muted-foreground mb-3">
              Specializing in natal charts, transits, and karmic astrology. 15+ years of cosmic guidance.
            </p>

            <div className="flex items-center gap-4 text-sm">
              <div className="flex items-center gap-1">
                <Calendar className="h-4 w-4 text-mystical-bright" />
                <span className="text-foreground">3,200+ readings</span>
              </div>
              <div className="flex items-center gap-1">
                <Clock className="h-4 w-4 text-mystical-bright" />
                <span className="text-foreground">
                  {isOnline ? "Available Now" : `Next: ${nextAvailable}`}
                </span>
              </div>
            </div>
          </div>
        </div>

        <div className="space-y-3 mb-6">
          {consultationTypes.map((consultation, index) => (
            <motion.div
              key={consultation.type}
              initial={{ opacity: 0, x: -20 }}
              animate={{ opacity: 1, x: 0 }}
              transition={{ delay: index * 0.1 }}
              className={`flex items-center justify-between p-3 rounded-lg border ${
                consultation.popular
                  ? 'bg-mystical-mid/10 border-mystical-mid/30'
                  : 'bg-card/30 border-mystical-mid/10'
              }`}
            >
              <div className="flex items-center gap-3">
                <div>
                  <div className="flex items-center gap-2">
                    <span className="font-medium text-foreground">{consultation.type}</span>
                    {consultation.popular && (
                      <Badge className="bg-celestial-mid text-space-void text-xs">
                        Most Popular
                      </Badge>
                    )}
                  </div>
                  <div className="text-sm text-muted-foreground">{consultation.duration}</div>
                </div>
              </div>
              <div className="text-right">
                <div className="text-lg font-semibold text-celestial-mid">{consultation.price}</div>
              </div>
            </motion.div>
          ))}
        </div>

        <div className="grid grid-cols-2 gap-3">
          <Button 
            className={`relative ${
              isOnline 
                ? "bg-mystical-mid hover:bg-mystical-bright animate-pulse-glow" 
                : "bg-secondary hover:bg-secondary/80"
            }`}
            disabled={!isOnline}
          >
            <Video className="h-4 w-4 mr-2" />
            {isOnline ? "Book Now" : "Schedule"}
            
            {isOnline && (
              <motion.div
                className="absolute inset-0 rounded-md border-2 border-mystical-bright/50"
                animate={{
                  scale: [1, 1.05, 1],
                  opacity: [0.5, 0.8, 0.5],
                }}
                transition={{
                  duration: 2,
                  repeat: Infinity,
                  ease: "easeInOut",
                }}
              />
            )}
          </Button>
          
          <Button variant="secondary" className="bg-secondary/50 hover:bg-secondary/70">
            <MessageCircle className="h-4 w-4 mr-2" />
            Chat
          </Button>
        </div>

        {isOnline && (
          <motion.div
            initial={{ opacity: 0, y: 10 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ delay: 0.5 }}
            className="mt-4 p-3 bg-green-500/10 border border-green-500/20 rounded-lg"
          >
            <div className="flex items-center gap-2">
              <div className="w-2 h-2 bg-green-500 rounded-full animate-pulse" />
              <span className="text-sm text-green-400 font-medium">
                Available for immediate consultation
              </span>
            </div>
          </motion.div>
        )}
      </div>
    </Card>
  );
}