// src/components/DashaTimeline.tsx - PROFESSIONAL ASTROLOGY REDESIGN
import React, { useState, useMemo } from 'react';
import { motion, AnimatePresence } from 'framer-motion';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { Badge } from '@/components/ui/badge';
import { Button } from '@/components/ui/button';
import {
  Calendar, Clock, Star, Zap, Award, Shield, TrendingUp, Sparkles,
  ChevronDown, BookOpen, Target, Compass,
  Sun, Moon, Globe, Crown, Gem, Heart, Brain
} from 'lucide-react';

interface DashaPeriod {
  mahadashaLord?: string;
  antardashaLord?: string;
  startDate?: string;
  endDate?: string;
  interpretation?: string;
  lifeTheme?: string;
  opportunities?: string;
  challenges?: string;
  isCurrent?: boolean;
  remedies?: string;
  duration?: string;
  theme?: string;
}

interface DashaTimelineProps {
  dashaTable: DashaPeriod[];
}

// ✅ PROFESSIONAL: Subtle, refined animations
const REFINED_ANIMATIONS = {
  container: {
    hidden: { opacity: 0 },
    visible: {
      opacity: 1,
      transition: {
        staggerChildren: 0.06,
        delayChildren: 0.1
      }
    }
  },
  timelineItem: {
    hidden: { opacity: 0, y: 8 },
    visible: { opacity: 1, y: 0 }
  }
};

const SMOOTH_TRANSITION = {
  duration: 0.4,
  ease: "easeOut" as const
};

const SUBTLE_PULSE = {
  scale: [1, 1.02, 1],
  transition: {
    duration: 3,
    repeat: Infinity,
    ease: "easeInOut" as const
  }
};

// ✅ PROFESSIONAL: Elegant planet themes with sophisticated colors
const ELEGANT_PLANET_THEMES = {
  'Sun': {
    gradient: 'bg-gradient-to-br from-amber-600/20 to-orange-500/20',
    border: 'border-amber-500/30',
    shadow: 'shadow-amber-500/10',
    iconBg: 'bg-gradient-to-br from-amber-400 to-orange-500',
    icon: Sun,
    element: 'Fire',
    nature: 'Leadership & Authority',
    chakra: 'Solar Plexus',
    accentColor: 'text-amber-300'
  },
  'Moon': {
    gradient: 'bg-gradient-to-br from-slate-600/20 to-blue-500/20',
    border: 'border-slate-500/30',
    shadow: 'shadow-slate-500/10',
    iconBg: 'bg-gradient-to-br from-slate-400 to-blue-500',
    icon: Moon,
    element: 'Water',
    nature: 'Emotions & Intuition',
    chakra: 'Sacral',
    accentColor: 'text-slate-300'
  },
  'Mercury': {
    gradient: 'bg-gradient-to-br from-emerald-600/20 to-green-500/20',
    border: 'border-emerald-500/30',
    shadow: 'shadow-emerald-500/10',
    iconBg: 'bg-gradient-to-br from-emerald-400 to-green-500',
    icon: Brain,
    element: 'Earth',
    nature: 'Communication & Intelligence',
    chakra: 'Throat',
    accentColor: 'text-emerald-300'
  },
  'Venus': {
    gradient: 'bg-gradient-to-br from-rose-600/20 to-pink-500/20',
    border: 'border-rose-500/30',
    shadow: 'shadow-rose-500/10',
    iconBg: 'bg-gradient-to-br from-rose-400 to-pink-500',
    icon: Heart,
    element: 'Water',
    nature: 'Love & Beauty',
    chakra: 'Heart',
    accentColor: 'text-rose-300'
  },
  'Mars': {
    gradient: 'bg-gradient-to-br from-red-600/20 to-orange-500/20',
    border: 'border-red-500/30',
    shadow: 'shadow-red-500/10',
    iconBg: 'bg-gradient-to-br from-red-500 to-orange-500',
    icon: Target,
    element: 'Fire',
    nature: 'Energy & Action',
    chakra: 'Root',
    accentColor: 'text-red-300'
  },
  'Jupiter': {
    gradient: 'bg-gradient-to-br from-yellow-600/20 to-amber-500/20',
    border: 'border-yellow-500/30',
    shadow: 'shadow-yellow-500/10',
    iconBg: 'bg-gradient-to-br from-yellow-500 to-amber-500',
    icon: Crown,
    element: 'Fire',
    nature: 'Wisdom & Expansion',
    chakra: 'Crown',
    accentColor: 'text-yellow-300'
  },
  'Saturn': {
    gradient: 'bg-gradient-to-br from-indigo-600/20 to-slate-500/20',
    border: 'border-indigo-500/30',
    shadow: 'shadow-indigo-500/10',
    iconBg: 'bg-gradient-to-br from-indigo-500 to-slate-600',
    icon: Shield,
    element: 'Earth',
    nature: 'Discipline & Karma',
    chakra: 'Root',
    accentColor: 'text-indigo-300'
  },
  'Rahu': {
    gradient: 'bg-gradient-to-br from-gray-600/20 to-slate-500/20',
    border: 'border-gray-500/30',
    shadow: 'shadow-gray-500/10',
    iconBg: 'bg-gradient-to-br from-gray-600 to-slate-700',
    icon: Compass,
    element: 'Air',
    nature: 'Desires & Illusion',
    chakra: 'Third Eye',
    accentColor: 'text-gray-300'
  },
  'Ketu': {
    gradient: 'bg-gradient-to-br from-orange-600/20 to-amber-500/20',
    border: 'border-orange-500/30',
    shadow: 'shadow-orange-500/10',
    iconBg: 'bg-gradient-to-br from-orange-600 to-amber-600',
    icon: Gem,
    element: 'Fire',
    nature: 'Spirituality & Detachment',
    chakra: 'Crown',
    accentColor: 'text-orange-300'
  }
} as const;

export function DashaTimeline({ dashaTable }: DashaTimelineProps) {
  const [expandedPeriods, setExpandedPeriods] = useState<Set<number>>(new Set([0]));
  const [selectedView, setSelectedView] = useState<'timeline' | 'grid'>('timeline');

  const processedDashaData = useMemo(() => {
    if (!Array.isArray(dashaTable)) return [];

    return dashaTable
      .filter(dasha => dasha && typeof dasha === 'object')
      .map((dasha, index) => ({
        ...dasha,
        id: `${dasha.mahadashaLord || 'unknown'}-${index}`,
        planetTheme: ELEGANT_PLANET_THEMES[dasha.mahadashaLord as keyof typeof ELEGANT_PLANET_THEMES] || ELEGANT_PLANET_THEMES['Sun']
      }));
  }, [dashaTable]);

  const toggleExpanded = (index: number) => {
    setExpandedPeriods(prev => {
      const newExpanded = new Set(prev);
      if (newExpanded.has(index)) {
        newExpanded.delete(index);
      } else {
        newExpanded.add(index);
      }
      return newExpanded;
    });
  };

  const formatDate = (dateStr?: string): string => {
    if (!dateStr) return 'Unknown';
    try {
      return new Date(dateStr).toLocaleDateString('en-US', {
        year: 'numeric',
        month: 'short',
        day: 'numeric'
      });
    } catch {
      return dateStr;
    }
  };

  const safeString = (str?: string | null, fallback = 'Analysis pending'): string => {
    return str && typeof str === 'string' && str.trim() !== '' ? str : fallback;
  };

  // ✅ PROFESSIONAL: Elegant empty state
  if (processedDashaData.length === 0) {
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
                <div className="w-20 h-20 rounded-full bg-gradient-to-br from-amber-500/20 to-orange-600/20 border border-amber-500/30 flex items-center justify-center">
                  <Calendar className="h-10 w-10 text-amber-400/80" strokeWidth={1.5} />
                </div>
                <div className="absolute -top-1 -right-1 w-6 h-6 bg-gradient-to-br from-amber-400 to-orange-500 rounded-full flex items-center justify-center">
                  <Sparkles className="h-3 w-3 text-amber-900" />
                </div>
              </div>
            </motion.div>
            
            <h3 className="text-2xl font-serif font-medium text-slate-100 mb-3">
              Dasha Timeline Analysis
            </h3>
            <p className="text-slate-400 text-base leading-relaxed max-w-lg mx-auto mb-8">
              Your personalized planetary timeline awaits. Complete chart analysis will reveal 
              the cosmic timing of your most significant periods.
            </p>
            
            <div className="flex justify-center gap-4">
              <Badge variant="outline" className="bg-slate-800/50 text-slate-300 border-slate-600/50 px-4 py-1.5">
                Swiss Ephemeris
              </Badge>
              <Badge variant="outline" className="bg-slate-800/50 text-slate-300 border-slate-600/50 px-4 py-1.5">
                Vedic Methods
              </Badge>
            </div>
          </CardContent>
        </Card>
      </motion.div>
    );
  }

  return (
    <div className="max-w-6xl mx-auto space-y-8">
      {/* ✅ PROFESSIONAL: Elegant header */}
      <motion.div
        initial={{ opacity: 0, y: -12 }}
        animate={{ opacity: 1, y: 0 }}
        transition={{ ...SMOOTH_TRANSITION, duration: 0.6 }}
        className="text-center"
      >
        <div className="flex justify-center mb-6">
          <div className="relative">
            <div className="w-16 h-16 rounded-full bg-gradient-to-br from-amber-500/20 to-orange-600/20 border border-amber-500/30 flex items-center justify-center">
              <TrendingUp className="h-8 w-8 text-amber-400" strokeWidth={1.5} />
            </div>
            <motion.div
              animate={{ rotate: 360 }}
              transition={{ duration: 30, repeat: Infinity, ease: "linear" }}
              className="absolute -top-1 -right-1 w-6 h-6 bg-gradient-to-br from-amber-400 to-orange-500 rounded-full flex items-center justify-center"
            >
              <Star className="h-3 w-3 text-amber-900" />
            </motion.div>
          </div>
        </div>

        <h2 className="text-4xl md:text-5xl font-serif font-medium mb-4 text-slate-100">
          Vimshottari Dasha Timeline
        </h2>
        <p className="text-slate-400 text-xl max-w-4xl mx-auto leading-relaxed mb-8">
          Navigate through your cosmic timeline where ancient wisdom meets modern precision. 
          Each planetary period reveals the sacred timing of your soul's journey.
        </p>

        {/* ✅ PROFESSIONAL: Refined view toggle */}
        <div className="flex justify-center gap-4 mb-8">
          <div className="flex bg-slate-800/50 rounded-lg p-1 border border-slate-700/50">
            <Button
              variant={selectedView === 'timeline' ? 'default' : 'ghost'}
              size="sm"
              onClick={() => setSelectedView('timeline')}
              className={`${selectedView === 'timeline' 
                ? 'bg-slate-700 hover:bg-slate-600 text-slate-100' 
                : 'text-slate-400 hover:text-slate-200'}`}
            >
              <Calendar className="w-4 h-4 mr-2" />
              Timeline View
            </Button>
            <Button
              variant={selectedView === 'grid' ? 'default' : 'ghost'}
              size="sm"
              onClick={() => setSelectedView('grid')}
              className={`${selectedView === 'grid' 
                ? 'bg-slate-700 hover:bg-slate-600 text-slate-100' 
                : 'text-slate-400 hover:text-slate-200'}`}
            >
              <Globe className="w-4 h-4 mr-2" />
              Grid View
            </Button>
          </div>
        </div>

        <div className="flex justify-center gap-4 flex-wrap">
          <Badge className="bg-slate-700/50 text-slate-200 border-slate-600/50 px-4 py-1.5">
            {processedDashaData.length} Periods
          </Badge>
          <Badge className="bg-slate-700/50 text-slate-200 border-slate-600/50 px-4 py-1.5">
            NASA-JPL Precision
          </Badge>
          <Badge className="bg-slate-700/50 text-slate-200 border-slate-600/50 px-4 py-1.5">
            Traditional Methods
          </Badge>
        </div>
      </motion.div>

      {/* ✅ PROFESSIONAL: Timeline container */}
      <motion.div
        variants={REFINED_ANIMATIONS.container}
        initial="hidden"
        animate="visible"
        className={selectedView === 'timeline' ? 'space-y-6' : 'grid md:grid-cols-2 xl:grid-cols-3 gap-6'}
      >
        <AnimatePresence>
          {processedDashaData.map((dasha, index) => {
            const isExpanded = expandedPeriods.has(index);
            const isCurrent = Boolean(dasha.isCurrent);
            const planetTheme = dasha.planetTheme;
            const IconComponent = planetTheme.icon;

            return (
              <motion.div
                key={dasha.id}
                variants={REFINED_ANIMATIONS.timelineItem}
                transition={{ ...SMOOTH_TRANSITION, delay: index * 0.04 }}
                layout
                className="relative"
              >
                {/* Timeline connector */}
                {selectedView === 'timeline' && index < processedDashaData.length - 1 && (
                  <div className="absolute left-8 top-20 w-0.5 h-16 bg-gradient-to-b from-slate-600/50 to-transparent -z-10"></div>
                )}

                <motion.div
                  animate={isCurrent ? SUBTLE_PULSE : undefined}
                  className={`relative ${isCurrent ? 'z-10' : ''}`}
                >
                  <Card
                    className={`
                      group cursor-pointer transition-all duration-300 hover:shadow-lg
                      ${isCurrent
                        ? `${planetTheme.gradient} ${planetTheme.border} ${planetTheme.shadow}`
                        : 'bg-gradient-to-br from-slate-900 via-slate-800 to-slate-900 border-slate-700/50 hover:border-slate-600/50'
                      }
                    `}
                    onClick={() => toggleExpanded(index)}
                  >
                    <CardHeader className="pb-4">
                      <div className="flex items-center justify-between gap-4">
                        <div className="flex items-center gap-4 flex-1">
                          {/* ✅ PROFESSIONAL: Elegant planet avatar */}
                          <motion.div
                            whileHover={{
                              scale: 1.05,
                              rotate: 2,
                              transition: { duration: 0.2 }
                            }}
                            className="flex-shrink-0"
                          >
                            <div className={`
                              w-14 h-14 rounded-full ${planetTheme.iconBg} 
                              flex items-center justify-center shadow-lg border border-white/10
                            `}>
                              <IconComponent className="h-7 w-7 text-white" strokeWidth={1.5} />
                              {isCurrent && (
                                <motion.div
                                  animate={{ scale: [1, 1.2, 1], opacity: [0.6, 0, 0.6] }}
                                  transition={{ duration: 2.5, repeat: Infinity }}
                                  className="absolute inset-0 rounded-full border border-white/40"
                                />
                              )}
                            </div>
                          </motion.div>

                          <div className="flex-1 min-w-0">
                            <div className="flex items-center gap-2 mb-1">
                              <CardTitle className="text-lg font-serif font-medium text-slate-100">
                                {safeString(dasha.mahadashaLord, 'Unknown')} Mahadasha
                              </CardTitle>
                              {isCurrent && (
                                <Badge className="bg-gradient-to-r from-emerald-600 to-green-600 text-emerald-100 border-0 text-xs px-2 py-0.5">
                                  <Zap className="h-3 w-3 mr-1" />
                                  Active
                                </Badge>
                              )}
                            </div>

                            {safeString(dasha.antardashaLord) !== 'Analysis pending' && (
                              <p className="text-slate-400 text-sm mb-2">
                                {safeString(dasha.antardashaLord)} Sub-period
                              </p>
                            )}

                            <div className="flex items-center gap-4 flex-wrap">
                              <Badge variant="outline" className="text-slate-300 border-slate-600/50 text-xs px-2 py-0.5">
                                <Clock className="h-3 w-3 mr-1" />
                                {formatDate(dasha.startDate)} - {formatDate(dasha.endDate)}
                              </Badge>

                              <div className="flex items-center gap-2 text-xs text-slate-500">
                                <div className={`w-2 h-2 rounded-full ${planetTheme.iconBg}`}></div>
                                {planetTheme.element} • {planetTheme.chakra}
                              </div>
                            </div>
                          </div>
                        </div>

                        <motion.div
                          animate={{ rotate: isExpanded ? 180 : 0 }}
                          transition={{ duration: 0.2 }}
                          className="text-slate-500 group-hover:text-slate-400"
                        >
                          <ChevronDown className="h-5 w-5" />
                        </motion.div>
                      </div>

                      {/* ✅ PROFESSIONAL: Planet nature display */}
                      <div className="mt-4 p-3 rounded-lg bg-slate-800/30 border border-slate-700/30">
                        <div className="flex items-center gap-2 mb-2">
                          <div className={`w-3 h-3 rounded-full ${planetTheme.iconBg}`}></div>
                          <span className={`text-sm font-medium ${planetTheme.accentColor}`}>
                            {planetTheme.nature}
                          </span>
                        </div>
                        <p className="text-slate-400 text-sm leading-relaxed">
                          {safeString(dasha.lifeTheme || dasha.theme, 'Personal growth and spiritual development')}
                        </p>
                      </div>
                    </CardHeader>

                    {/* Expandable content */}
                    <AnimatePresence>
                      {isExpanded && (
                        <motion.div
                          initial={{ opacity: 0, height: 0 }}
                          animate={{ opacity: 1, height: 'auto' }}
                          exit={{ opacity: 0, height: 0 }}
                          transition={{ duration: 0.3 }}
                        >
                          <CardContent className="pt-0 space-y-6">
                            {/* Opportunities & Challenges */}
                            <div className="grid md:grid-cols-2 gap-4">
                              <div className="p-4 rounded-lg bg-gradient-to-br from-emerald-600/15 to-green-500/15 border border-emerald-500/20">
                                <h4 className="font-medium text-emerald-300 flex items-center gap-2 mb-3">
                                  <Star className="h-4 w-4" strokeWidth={1.5} />
                                  Sacred Opportunities
                                </h4>
                                <p className="text-sm text-slate-300 leading-relaxed">
                                  {safeString(dasha.opportunities, 'Enhanced potential for positive developments and spiritual growth')}
                                </p>
                              </div>

                              <div className="p-4 rounded-lg bg-gradient-to-br from-orange-600/15 to-red-500/15 border border-orange-500/20">
                                <h4 className="font-medium text-orange-300 flex items-center gap-2 mb-3">
                                  <Shield className="h-4 w-4" strokeWidth={1.5} />
                                  Karmic Lessons
                                </h4>
                                <p className="text-sm text-slate-300 leading-relaxed">
                                  {safeString(dasha.challenges, 'Areas requiring mindful attention and conscious growth')}
                                </p>
                              </div>
                            </div>

                            {/* Interpretation & Remedies */}
                            <div className="space-y-4">
                              <div className="p-4 rounded-lg bg-slate-800/40 border border-slate-700/30">
                                <h4 className="font-medium text-slate-300 mb-3 flex items-center gap-2">
                                  <BookOpen className="h-4 w-4" strokeWidth={1.5} />
                                  Vedic Interpretation
                                </h4>
                                <p className="text-sm text-slate-400 leading-relaxed">
                                  {safeString(dasha.interpretation, 'This period brings profound opportunities for growth based on traditional Vedic principles.')}
                                </p>
                              </div>

                              <div className="p-4 rounded-lg bg-gradient-to-r from-amber-600/15 to-orange-500/15 border border-amber-500/20">
                                <h5 className="text-sm font-medium text-amber-300 mb-3 flex items-center gap-2">
                                  <Gem className="h-4 w-4" strokeWidth={1.5} />
                                  Sacred Remedies
                                </h5>
                                <p className="text-sm text-slate-400 leading-relaxed">
                                  {safeString(dasha.remedies, 'Traditional Vedic remedies and practices to harmonize planetary energies.')}
                                </p>
                              </div>
                            </div>
                          </CardContent>
                        </motion.div>
                      )}
                    </AnimatePresence>
                  </Card>
                </motion.div>
              </motion.div>
            );
          })}
        </AnimatePresence>
      </motion.div>

      {/* ✅ PROFESSIONAL: Educational footer */}
      <motion.div
        initial={{ opacity: 0, y: 12 }}
        animate={{ opacity: 1, y: 0 }}
        transition={{ ...SMOOTH_TRANSITION, delay: 0.6 }}
        className="mt-12"
      >
        <Card className="bg-gradient-to-br from-slate-900 via-slate-800 to-slate-900 border border-slate-700/50">
          <CardContent className="p-8">
            <div className="text-center mb-6">
              <div className="flex justify-center mb-4">
                <div className="w-12 h-12 rounded-lg bg-gradient-to-br from-blue-600/20 to-indigo-500/20 border border-blue-500/30 flex items-center justify-center">
                  <Award className="h-6 w-6 text-blue-400" strokeWidth={1.5} />
                </div>
              </div>
              <h4 className="text-xl font-serif font-medium text-slate-100 mb-3">
                Understanding Vimshottari Dasha
              </h4>
              <p className="text-slate-400 leading-relaxed max-w-3xl mx-auto">
                The Vimshottari Dasha system represents the crown jewel of Vedic timing techniques. 
                This 120-year cycle reveals when specific karmic patterns manifest, offering profound 
                insights into your soul's evolutionary journey.
              </p>
            </div>

            <div className="grid md:grid-cols-3 gap-6 mb-6">
              <div className="text-center p-4 rounded-lg bg-slate-800/30 border border-slate-700/30">
                <Globe className="h-8 w-8 text-blue-400 mx-auto mb-2" strokeWidth={1.5} />
                <h5 className="font-medium text-slate-200 mb-1">Swiss Ephemeris</h5>
                <p className="text-xs text-slate-500">NASA-JPL precision calculations</p>
              </div>
              <div className="text-center p-4 rounded-lg bg-slate-800/30 border border-slate-700/30">
                <Crown className="h-8 w-8 text-amber-400 mx-auto mb-2" strokeWidth={1.5} />
                <h5 className="font-medium text-slate-200 mb-1">Ancient Wisdom</h5>
                <p className="text-xs text-slate-500">5000+ years of Vedic knowledge</p>
              </div>
              <div className="text-center p-4 rounded-lg bg-slate-800/30 border border-slate-700/30">
                <Sparkles className="h-8 w-8 text-emerald-400 mx-auto mb-2" strokeWidth={1.5} />
                <h5 className="font-medium text-slate-200 mb-1">Personal Growth</h5>
                <p className="text-xs text-slate-500">Tailored spiritual guidance</p>
              </div>
            </div>

            <div className="flex justify-center text-xs text-slate-500">
              Swiss Ephemeris • Traditional Vedic Methods • Professional Analysis
            </div>
          </CardContent>
        </Card>
      </motion.div>
    </div>
  );
}
