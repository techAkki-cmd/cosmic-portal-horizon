import React from 'react';
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card';
import { Badge } from '@/components/ui/badge';
import { Calendar, Clock, Star, Zap } from 'lucide-react';

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
}

interface DashaTimelineProps {
  dashaTable: DashaPeriod[];
}

export function DashaTimeline({ dashaTable }: DashaTimelineProps) {
  // ✅ CRITICAL FIX: Add validation and fallback
  const safeDashaTable = Array.isArray(dashaTable) ? dashaTable : [];
  
  if (safeDashaTable.length === 0) {
    return (
      <Card className="p-8 text-center">
        <Calendar className="h-12 w-12 text-muted-foreground mx-auto mb-4" />
        <h3 className="text-xl font-semibold mb-2">Dasha Timeline</h3>
        <p className="text-muted-foreground">
          Complete your birth chart to view your personalized Vimshottari dasha periods.
        </p>
      </Card>
    );
  }

  // ✅ SAFE STRING ACCESS: Helper function to safely get first character
  const getFirstChar = (str: string | undefined | null): string => {
    if (!str || typeof str !== 'string' || str.length === 0) {
      return '?';
    }
    return str.charAt(0).toUpperCase();
  };

  // ✅ SAFE STRING ACCESS: Helper function to safely format strings
  const safeString = (str: string | undefined | null, fallback: string = 'Unknown'): string => {
    return str && typeof str === 'string' && str.trim() !== '' ? str : fallback;
  };

  return (
    <div className="space-y-6">
      <div className="text-center mb-6">
        <h3 className="text-2xl font-bold mb-2">Vimshottari Dasha Timeline</h3>
        <p className="text-muted-foreground">
          Your planetary periods and their influences on life events
        </p>
      </div>

      <div className="space-y-4">
        {safeDashaTable.map((dasha, index) => {
          // ✅ SAFE ACCESS: Validate each dasha object
          const mahadasha = safeString(dasha?.mahadashaLord);
          const antardasha = safeString(dasha?.antardashaLord);
          const startDate = safeString(dasha?.startDate);
          const endDate = safeString(dasha?.endDate);
          const interpretation = safeString(dasha?.interpretation, 'Dasha interpretation pending');
          const lifeTheme = safeString(dasha?.lifeTheme, 'Life theme analysis');
          const opportunities = safeString(dasha?.opportunities, 'Opportunities to be revealed');
          const challenges = safeString(dasha?.challenges, 'Challenges to be identified');
          const remedies = safeString(dasha?.remedies, 'Remedies to be prescribed');
          const isCurrent = Boolean(dasha?.isCurrent);

          return (
            <Card 
              key={`${mahadasha}-${antardasha}-${index}`}
              className={`transition-all duration-300 hover:shadow-lg ${
                isCurrent 
                  ? 'bg-gradient-to-r from-purple-500/10 to-pink-500/10 border-purple-500/30' 
                  : 'bg-card/80 backdrop-blur-sm'
              }`}
            >
              <CardHeader>
                <div className="flex items-center justify-between">
                  <CardTitle className="flex items-center gap-3">
                    <div className="flex items-center gap-2">
                      {/* ✅ SAFE CHAR ACCESS: Using helper function */}
                      <div className="w-10 h-10 rounded-full bg-gradient-to-br from-purple-500 to-pink-500 flex items-center justify-center text-white font-bold">
                        {getFirstChar(mahadasha)}
                      </div>
                      <div>
                        <div className="text-lg font-bold">
                          {mahadasha} Mahadasha
                        </div>
                        {antardasha !== 'Unknown' && (
                          <div className="text-sm text-muted-foreground">
                            {antardasha} Antardasha
                          </div>
                        )}
                      </div>
                    </div>
                  </CardTitle>
                  
                  <div className="flex items-center gap-2">
                    {isCurrent && (
                      <Badge variant="secondary" className="bg-green-500/20 text-green-300">
                        <Zap className="h-3 w-3 mr-1" />
                        Current
                      </Badge>
                    )}
                    <Badge variant="outline" className="text-muted-foreground">
                      <Clock className="h-3 w-3 mr-1" />
                      {startDate} - {endDate}
                    </Badge>
                  </div>
                </div>
                
                <CardDescription className="text-base leading-relaxed">
                  <strong>Theme:</strong> {lifeTheme}
                </CardDescription>
              </CardHeader>
              
              <CardContent className="space-y-4">
                <div className="grid md:grid-cols-2 gap-6">
                  <div className="space-y-3">
                    <h4 className="font-semibold text-green-400 flex items-center gap-2">
                      <Star className="h-4 w-4" />
                      Opportunities
                    </h4>
                    <p className="text-sm text-muted-foreground leading-relaxed">
                      {opportunities}
                    </p>
                  </div>
                  
                  <div className="space-y-3">
                    <h4 className="font-semibold text-orange-400 flex items-center gap-2">
                      <Zap className="h-4 w-4" />
                      Challenges
                    </h4>
                    <p className="text-sm text-muted-foreground leading-relaxed">
                      {challenges}
                    </p>
                  </div>
                </div>
                
                <div className="pt-4 border-t border-border/50">
                  <h4 className="font-semibold text-purple-400 mb-2">Interpretation</h4>
                  <p className="text-sm text-muted-foreground leading-relaxed mb-3">
                    {interpretation}
                  </p>
                  
                  <div className="bg-white/5 rounded-lg p-3 border border-white/10">
                    <h5 className="text-xs font-medium text-muted-foreground mb-1">
                      Recommended Remedies:
                    </h5>
                    <p className="text-xs text-muted-foreground">
                      {remedies}
                    </p>
                  </div>
                </div>
              </CardContent>
            </Card>
          );
        })}
      </div>
    </div>
  );
}
