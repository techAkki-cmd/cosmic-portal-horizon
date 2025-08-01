import React from 'react';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { Badge } from '@/components/ui/badge';
import { Star, Crown, Sparkles, AlertTriangle } from 'lucide-react';
import { motion } from 'framer-motion';

interface RareYoga {
  name: string;
  description: string;
  meaning: string;
  planetsCombination?: string;
  isVeryRare: boolean;
  remedialAction?: string;
  rarity?: number;
}

interface Props {
  yogas: RareYoga[];
}

export function RareYogaHighlights({ yogas }: Props) {
  if (!yogas || yogas.length === 0) {
    return (
      <Card className="bg-card/80 backdrop-blur-sm border-mystical-mid/20">
        <CardContent className="p-6 text-center">
          <Sparkles className="h-12 w-12 text-muted-foreground mx-auto mb-3" />
          <p className="text-muted-foreground">No rare yogas detected in this chart.</p>
          <p className="text-sm text-muted-foreground mt-2">
            This doesn't diminish the chart's value - every chart has its unique strengths.
          </p>
        </CardContent>
      </Card>
    );
  }

  const getYogaIcon = (yoga: RareYoga) => {
    if (yoga.name.includes('Raj')) return <Crown className="h-5 w-5" />;
    if (yoga.name.includes('Gaja')) return <Star className="h-5 w-5" />;
    if (yoga.name.includes('Kemadruma')) return <AlertTriangle className="h-5 w-5" />;
    return <Sparkles className="h-5 w-5" />;
  };

  const getYogaColor = (yoga: RareYoga) => {
    if (yoga.name.includes('Kemadruma')) return 'from-orange-500 to-red-500';
    if (yoga.isVeryRare) return 'from-purple-500 to-pink-500';
    return 'from-blue-500 to-indigo-500';
  };

  return (
    <Card className="bg-card/80 backdrop-blur-sm border-mystical-mid/20">
      <CardHeader>
        <CardTitle className="flex items-center gap-2 text-foreground">
          <Crown className="h-5 w-5 text-celestial-mid" />
          Rare Yogas Detected ({yogas.length})
        </CardTitle>
      </CardHeader>
      <CardContent className="space-y-4">
        {yogas.map((yoga, index) => (
          <motion.div
            key={index}
            initial={{ opacity: 0, y: 20 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ delay: index * 0.1 }}
            className={`p-4 rounded-xl bg-gradient-to-br ${getYogaColor(yoga)}/10 border border-white/20 hover:border-white/40 transition-all duration-300`}
          >
            <div className="flex items-start gap-3">
              <div className={`text-transparent bg-gradient-to-br ${getYogaColor(yoga)} bg-clip-text`}>
                {getYogaIcon(yoga)}
              </div>
              <div className="flex-1">
                <div className="flex items-center gap-2 mb-2">
                  <h4 className="font-semibold text-foreground">{yoga.name}</h4>
                  {yoga.isVeryRare && (
                    <Badge variant="secondary" className="bg-gradient-to-r from-purple-400 to-pink-400 text-white border-0 text-xs">
                      <Crown className="w-3 h-3 mr-1" />
                      VERY RARE
                    </Badge>
                  )}
                  {yoga.rarity && (
                    <Badge variant="outline" className="text-xs">
                      {yoga.rarity}% have this
                    </Badge>
                  )}
                </div>
                
                <p className="text-sm text-muted-foreground mb-2 leading-relaxed">
                  {yoga.description}
                </p>
                
                <div className="space-y-2">
                  <div>
                    <span className="text-xs font-medium text-foreground">Meaning:</span>
                    <p className="text-sm text-muted-foreground">{yoga.meaning}</p>
                  </div>
                  
                  {yoga.planetsCombination && (
                    <div>
                      <span className="text-xs font-medium text-foreground">Combination:</span>
                      <p className="text-sm text-muted-foreground">{yoga.planetsCombination}</p>
                    </div>
                  )}
                  
                  {yoga.remedialAction && (
                    <div className="mt-3 p-3 rounded-lg bg-white/50 backdrop-blur-sm">
                      <span className="text-xs font-medium text-foreground">Remedial Action:</span>
                      <p className="text-sm text-muted-foreground mt-1">{yoga.remedialAction}</p>
                    </div>
                  )}
                </div>
              </div>
            </div>
          </motion.div>
        ))}
        
        <div className="mt-6 p-4 rounded-lg bg-gradient-to-r from-celestial-light/20 to-mystical-light/20 backdrop-blur-sm border border-white/10">
          <h4 className="font-semibold text-foreground mb-2 flex items-center gap-2">
            <Sparkles className="h-4 w-4" />
            Understanding Yogas
          </h4>
          <p className="text-sm text-muted-foreground leading-relaxed">
            Yogas are special planetary combinations in Vedic astrology that create unique life patterns. 
            The presence of rare yogas indicates specific karmic influences and potential life themes. 
            Use this knowledge for self-awareness and spiritual growth.
          </p>
        </div>
      </CardContent>
    </Card>
  );
}
