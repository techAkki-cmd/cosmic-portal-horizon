// src/components/RareYogaHighlights.tsx - PROFESSIONAL ASTROLOGY DESIGN
import React from 'react';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { Badge } from '@/components/ui/badge';
import { Star, Crown, Sparkles, AlertTriangle, Shield, Award, Zap, Gem } from 'lucide-react';
import { motion, AnimatePresence } from 'framer-motion';

// ✅ PROFESSIONAL: Interface remains the same but styling is completely refined
interface RareYoga {
  name: string;
  description: string;
  meaning?: string;
  planetsCombination?: string;
  isVeryRare: boolean;
  remedialAction?: string;
  rarity?: number;
  strength?: string;
}

interface Props {
  yogas: RareYoga[];
}

// ✅ PROFESSIONAL: Subtle, refined animations
const REFINED_ANIMATIONS = {
  container: {
    hidden: { opacity: 0 },
    visible: {
      opacity: 1,
      transition: {
        staggerChildren: 0.08,
        delayChildren: 0.1
      }
    }
  },
  item: {
    hidden: { opacity: 0, y: 8 },
    visible: { opacity: 1, y: 0 }
  }
};

const SMOOTH_TRANSITION = {
  duration: 0.4,
  ease: "easeOut" as const
};

export function RareYogaHighlights({ yogas }: Props) {
  // ✅ PROFESSIONAL: Elegant empty state
  if (!yogas || yogas.length === 0) {
    return (
      <motion.div
        initial={{ opacity: 0, y: 12 }}
        animate={{ opacity: 1, y: 0 }}
        transition={SMOOTH_TRANSITION}
        className="max-w-4xl mx-auto"
      >
        <Card className="bg-gradient-to-br from-slate-900 via-slate-800 to-slate-900 border border-slate-700/50 shadow-xl">
          <CardContent className="p-16 text-center">
            <motion.div
              initial={{ scale: 0 }}
              animate={{ scale: 1 }}
              transition={{ type: "spring", stiffness: 300, damping: 25, delay: 0.2 }}
              className="mb-8"
            >
              <div className="relative inline-block">
                <div className="w-20 h-20 rounded-full bg-gradient-to-br from-amber-500/20 to-yellow-600/20 border border-amber-500/30 flex items-center justify-center">
                  <Sparkles className="h-10 w-10 text-amber-400/80" strokeWidth={1.5} />
                </div>
                <div className="absolute -top-1 -right-1 w-6 h-6 bg-gradient-to-br from-amber-400 to-yellow-500 rounded-full flex items-center justify-center">
                  <Star className="h-3 w-3 text-amber-900" />
                </div>
              </div>
            </motion.div>
            
            <h3 className="text-2xl font-serif font-medium text-slate-100 mb-3">
              Yogas Under Analysis
            </h3>
            <p className="text-slate-400 text-base leading-relaxed max-w-lg mx-auto">
              Your birth chart is being examined for rare planetary combinations. 
              Complete chart analysis will reveal all significant yogas.
            </p>
            
            <div className="flex justify-center gap-4 mt-8">
              <Badge variant="outline" className="bg-slate-800/50 text-slate-300 border-slate-600/50 px-4 py-1.5">
                Swiss Ephemeris
              </Badge>
              <Badge variant="outline" className="bg-slate-800/50 text-slate-300 border-slate-600/50 px-4 py-1.5">
                Traditional Methods
              </Badge>
            </div>
          </CardContent>
        </Card>
      </motion.div>
    );
  }

  // ✅ PROFESSIONAL: Elegant yoga categorization
  const getYogaTheme = (yoga: RareYoga) => {
    const name = yoga.name.toLowerCase();
    
    if (name.includes('raj') || name.includes('royal')) {
      return {
        icon: Crown,
        gradient: 'from-amber-600/20 to-yellow-500/20',
        border: 'border-amber-500/30',
        iconColor: 'text-amber-400',
        accent: 'text-amber-300'
      };
    }
    
    if (name.includes('gaja') || name.includes('kesari')) {
      return {
        icon: Star,
        gradient: 'from-yellow-600/20 to-orange-500/20',
        border: 'border-yellow-500/30',
        iconColor: 'text-yellow-400',
        accent: 'text-yellow-300'
      };
    }
    
    if (name.includes('dhana') || name.includes('wealth')) {
      return {
        icon: Gem,
        gradient: 'from-emerald-600/20 to-green-500/20',
        border: 'border-emerald-500/30',
        iconColor: 'text-emerald-400',
        accent: 'text-emerald-300'
      };
    }
    
    if (name.includes('hamsa') || name.includes('spiritual')) {
      return {
        icon: Sparkles,
        gradient: 'from-blue-600/20 to-indigo-500/20',
        border: 'border-blue-500/30',
        iconColor: 'text-blue-400',
        accent: 'text-blue-300'
      };
    }
    
    if (name.includes('kemadruma') || name.includes('malefic')) {
      return {
        icon: AlertTriangle,
        gradient: 'from-red-600/20 to-orange-500/20',
        border: 'border-red-500/30',
        iconColor: 'text-red-400',
        accent: 'text-red-300'
      };
    }
    
    // Default theme
    return {
      icon: Award,
      gradient: 'from-slate-600/20 to-slate-500/20',
      border: 'border-slate-500/30',
      iconColor: 'text-slate-400',
      accent: 'text-slate-300'
    };
  };

  const getRarityLevel = (rarity?: number): { label: string; color: string } => {
    if (!rarity) return { label: '', color: '' };
    if (rarity < 1) return { label: 'Legendary', color: 'bg-amber-600/20 text-amber-300 border-amber-500/40' };
    if (rarity < 5) return { label: 'Extremely Rare', color: 'bg-orange-600/20 text-orange-300 border-orange-500/40' };
    if (rarity < 15) return { label: 'Very Rare', color: 'bg-yellow-600/20 text-yellow-300 border-yellow-500/40' };
    if (rarity < 30) return { label: 'Rare', color: 'bg-blue-600/20 text-blue-300 border-blue-500/40' };
    if (rarity < 50) return { label: 'Uncommon', color: 'bg-slate-600/20 text-slate-300 border-slate-500/40' };
    return { label: 'Common', color: 'bg-slate-700/20 text-slate-400 border-slate-600/40' };
  };

  const getStrengthIndicator = (strength?: string) => {
    if (!strength) return null;
    
    const strengthStyles = {
      excellent: { bg: 'bg-emerald-700/30', text: 'text-emerald-200', border: 'border-emerald-500/50' },
      strong: { bg: 'bg-cyan-700/30', text: 'text-cyan-200', border: 'border-cyan-500/50' },
      moderate: { bg: 'bg-amber-700/30', text: 'text-amber-200', border: 'border-amber-500/50' },
      weak: { bg: 'bg-red-700/30', text: 'text-red-200', border: 'border-red-500/50' }
    };
    
    const style = strengthStyles[strength.toLowerCase() as keyof typeof strengthStyles] || strengthStyles.moderate;
    
    return (
      <div className={`px-3 py-1 rounded-md border ${style.bg} ${style.text} ${style.border} text-xs font-medium`}>
        {strength.charAt(0).toUpperCase() + strength.slice(1)}
      </div>
    );
  };

  return (
    <motion.div
      variants={REFINED_ANIMATIONS.container}
      initial="hidden"
      animate="visible"
      className="max-w-5xl mx-auto"
    >
      <Card className="bg-gradient-to-br from-slate-900 via-slate-800 to-slate-900 border border-slate-700/50 shadow-2xl">
        <CardHeader className="border-b border-slate-700/50">
          <div className="flex items-center justify-between">
            <motion.div 
              className="flex items-center gap-4"
              initial={{ opacity: 0, x: -12 }}
              animate={{ opacity: 1, x: 0 }}
              transition={{ ...SMOOTH_TRANSITION, delay: 0.1 }}
            >
              <div className="w-12 h-12 rounded-lg bg-gradient-to-br from-amber-600/20 to-yellow-500/20 border border-amber-500/30 flex items-center justify-center">
                <Crown className="h-6 w-6 text-amber-400" strokeWidth={1.5} />
              </div>
              <div>
                <CardTitle className="text-xl font-serif font-medium text-slate-100">
                  Rare Yogas Analysis
                </CardTitle>
                <p className="text-slate-400 text-sm mt-0.5">
                  Precision calculations via Swiss Ephemeris
                </p>
              </div>
            </motion.div>
            
            <motion.div
              initial={{ opacity: 0, scale: 0.9 }}
              animate={{ opacity: 1, scale: 1 }}
              transition={{ ...SMOOTH_TRANSITION, delay: 0.2 }}
            >
              <Badge className="bg-slate-700/50 text-slate-200 border-slate-600/50 px-4 py-1.5 font-medium">
                {yogas.length} Detected
              </Badge>
            </motion.div>
          </div>
        </CardHeader>
        
        <CardContent className="p-8">
          <div className="space-y-6">
            <AnimatePresence>
              {yogas.map((yoga, index) => {
                const theme = getYogaTheme(yoga);
                const IconComponent = theme.icon;
                const rarityInfo = getRarityLevel(yoga.rarity);

                return (
                  <motion.div
                    key={`${yoga.name}-${index}`}
                    variants={REFINED_ANIMATIONS.item}
                    transition={{ ...SMOOTH_TRANSITION, delay: index * 0.05 }}
                    whileHover={{ 
                      y: -2,
                      transition: { duration: 0.2 }
                    }}
                    className="group"
                  >
                    <Card className={`
                      bg-gradient-to-r ${theme.gradient} 
                      border ${theme.border} 
                      hover:border-slate-500/50 
                      transition-all duration-300 
                      shadow-sm hover:shadow-lg
                    `}>
                      <CardContent className="p-6">
                        <div className="flex items-start gap-5">
                          {/* Icon */}
                          <motion.div
                            whileHover={{ 
                              rotate: 5,
                              transition: { duration: 0.2 }
                            }}
                            className="flex-shrink-0"
                          >
                            <div className={`
                              w-12 h-12 rounded-lg 
                              bg-gradient-to-br from-slate-800/50 to-slate-700/50 
                              border ${theme.border}
                              flex items-center justify-center
                            `}>
                              <IconComponent className={`h-6 w-6 ${theme.iconColor}`} strokeWidth={1.5} />
                            </div>
                          </motion.div>
                          
                          {/* Content */}
                          <div className="flex-1 min-w-0">
                            {/* Title and badges */}
                            <div className="flex items-start justify-between mb-3">
                              <div>
                                <h4 className="text-lg font-serif font-medium text-slate-100 mb-1">
                                  {yoga.name}
                                </h4>
                                <div className="flex items-center gap-2 flex-wrap">
                                  {yoga.isVeryRare && (
                                    <Badge className="bg-gradient-to-r from-amber-600 to-yellow-600 text-amber-100 border-0 text-xs px-2 py-0.5">
                                      <Crown className="w-3 h-3 mr-1" />
                                      Exceptional
                                    </Badge>
                                  )}
                                  
                                  {yoga.rarity && (
                                    <Badge 
                                      variant="outline" 
                                      className={`text-xs px-2 py-0.5 ${rarityInfo.color}`}
                                    >
                                      {yoga.rarity}% prevalence
                                    </Badge>
                                  )}
                                </div>
                              </div>
                              
                              <div className="flex items-center gap-2">
                                {getStrengthIndicator(yoga.strength)}
                              </div>
                            </div>
                            
                            {/* Description */}
                            <p className="text-slate-300 leading-relaxed mb-4 text-sm">
                              {yoga.description}
                            </p>
                            
                            {/* Details sections */}
                            <div className="space-y-3">
                              {yoga.meaning && (
                                <div className="p-3 rounded-lg bg-slate-800/30 border border-slate-700/30">
                                  <div className="flex items-center gap-2 mb-1">
                                    <Sparkles className={`h-3 w-3 ${theme.accent}`} />
                                    <span className={`text-xs font-medium ${theme.accent}`}>
                                      Significance
                                    </span>
                                  </div>
                                  <p className="text-slate-400 text-xs leading-relaxed">
                                    {yoga.meaning}
                                  </p>
                                </div>
                              )}
                              
                              {yoga.planetsCombination && (
                                <div className="p-3 rounded-lg bg-slate-800/30 border border-slate-700/30">
                                  <div className="flex items-center gap-2 mb-1">
                                    <Star className="h-3 w-3 text-blue-300" />
                                    <span className="text-xs font-medium text-blue-300">
                                      Planetary Formation
                                    </span>
                                  </div>
                                  <p className="text-slate-400 text-xs">
                                    {yoga.planetsCombination}
                                  </p>
                                </div>
                              )}
                              
                              {yoga.remedialAction && (
                                <div className="p-3 rounded-lg bg-gradient-to-r from-slate-800/40 to-slate-700/40 border border-slate-600/30">
                                  <div className="flex items-center gap-2 mb-1">
                                    <Award className="h-3 w-3 text-emerald-300" />
                                    <span className="text-xs font-medium text-emerald-300">
                                      Remedial Guidance
                                    </span>
                                  </div>
                                  <p className="text-slate-400 text-xs leading-relaxed">
                                    {yoga.remedialAction}
                                  </p>
                                </div>
                              )}
                            </div>
                          </div>
                        </div>
                      </CardContent>
                    </Card>
                  </motion.div>
                );
              })}
            </AnimatePresence>
          </div>
          
          {/* Professional footer */}
          <motion.div
            initial={{ opacity: 0, y: 12 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ ...SMOOTH_TRANSITION, delay: yogas.length * 0.05 + 0.3 }}
            className="mt-12 p-6 rounded-lg bg-gradient-to-r from-slate-800/30 to-slate-700/30 border border-slate-600/30"
          >
            <div className="flex items-start gap-4">
              <div className="w-10 h-10 rounded-lg bg-gradient-to-br from-blue-600/20 to-indigo-500/20 border border-blue-500/30 flex items-center justify-center flex-shrink-0">
                <Award className="h-5 w-5 text-blue-400" strokeWidth={1.5} />
              </div>
              <div>
                <h4 className="font-serif font-medium text-slate-100 mb-2">
                  About Vedic Yogas
                </h4>
                <p className="text-slate-400 text-sm leading-relaxed mb-4">
                  Yogas represent specific planetary combinations that influence life patterns, opportunities, and spiritual evolution. 
                  Each formation carries unique significance in the cosmic blueprint of your existence.
                </p>
                <div className="flex items-center gap-4 text-xs">
                  <span className="text-slate-500">Swiss Ephemeris • NASA-JPL Precision • Traditional Vedic Methods</span>
                </div>
              </div>
            </div>
          </motion.div>
        </CardContent>
      </Card>
    </motion.div>
  );
}
