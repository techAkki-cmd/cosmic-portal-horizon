import React from 'react';
import { Navigation } from '@/components/Navigation';
import { Footer } from '@/components/Footer';
import { QuickBirthChart } from '@/components/QuickBirthChart';
import { CosmicBackground } from '@/components/CosmicBackground';
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card';
import { Moon, Star, Sparkles } from 'lucide-react';
import { motion } from 'framer-motion';

export default function BirthChart() {
  return (
    <div className="min-h-screen bg-background text-foreground">
      <CosmicBackground />
      <Navigation />
      
      <main className="pt-20 pb-16 px-6">
        <div className="max-w-4xl mx-auto">
          <motion.div
            initial={{ opacity: 0, y: 30 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ duration: 0.8 }}
            className="text-center mb-12"
          >
            <div className="flex justify-center mb-6">
              <div className="p-4 rounded-full bg-gradient-mystical">
                <Moon className="h-12 w-12 text-white" />
              </div>
            </div>
            <h1 className="text-3xl md:text-4xl font-playfair font-bold text-foreground mb-4">
              Birth Chart Generator
            </h1>
            <p className="text-muted-foreground text-lg max-w-2xl mx-auto">
              Discover the cosmic blueprint of your personality, strengths, and life path 
              through your personalized natal chart analysis.
            </p>
          </motion.div>

          <div className="grid lg:grid-cols-3 gap-8">
            {/* Main Chart Generator */}
            <motion.div
              initial={{ opacity: 0, x: -30 }}
              animate={{ opacity: 1, x: 0 }}
              transition={{ duration: 0.8, delay: 0.2 }}
              className="lg:col-span-2"
            >
              <QuickBirthChart />
            </motion.div>

            {/* Information Sidebar */}
            <motion.div
              initial={{ opacity: 0, x: 30 }}
              animate={{ opacity: 1, x: 0 }}
              transition={{ duration: 0.8, delay: 0.4 }}
              className="space-y-6"
            >
              <Card className="bg-card/80 backdrop-blur-sm border-mystical-mid/20">
                <CardHeader>
                  <CardTitle className="flex items-center gap-2 text-foreground">
                    <Star className="h-5 w-5 text-celestial-mid" />
                    What You'll Discover
                  </CardTitle>
                </CardHeader>
                <CardContent className="space-y-4">
                  {[
                    {
                      icon: <Moon className="h-5 w-5" />,
                      title: "Sun, Moon & Rising Signs",
                      description: "Your core personality, emotions, and first impressions"
                    },
                    {
                      icon: <Sparkles className="h-5 w-5" />,
                      title: "Planetary Positions",
                      description: "Where each planet was at your birth and what it means"
                    },
                    {
                      icon: <Star className="h-5 w-5" />,
                      title: "House Placements",
                      description: "Life areas where planetary energies manifest"
                    }
                  ].map((item, index) => (
                    <div key={index} className="flex gap-3">
                      <div className="text-celestial-mid mt-1">
                        {item.icon}
                      </div>
                      <div>
                        <h4 className="font-medium text-foreground text-sm mb-1">
                          {item.title}
                        </h4>
                        <p className="text-muted-foreground text-xs">
                          {item.description}
                        </p>
                      </div>
                    </div>
                  ))}
                </CardContent>
              </Card>

              <Card className="bg-card/80 backdrop-blur-sm border-mystical-mid/20">
                <CardHeader>
                  <CardTitle className="flex items-center gap-2 text-foreground">
                    <Sparkles className="h-5 w-5 text-celestial-mid" />
                    Birth Time Accuracy
                  </CardTitle>
                </CardHeader>
                <CardContent>
                  <p className="text-muted-foreground text-sm leading-relaxed">
                    For the most accurate reading, try to provide your exact birth time. 
                    You can usually find this on your birth certificate. If unknown, 
                    we'll create a chart for noon, which will still provide valuable insights.
                  </p>
                </CardContent>
              </Card>
            </motion.div>
          </div>
        </div>
      </main>

      <Footer />
    </div>
  );
}