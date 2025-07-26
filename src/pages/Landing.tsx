import React from 'react';
import { Link } from 'react-router-dom';
import { Button } from '@/components/ui/button';
import { CosmicBackground } from '@/components/CosmicBackground';
import { VoiceSearch } from '@/components/VoiceSearch';
import { PlanetaryTicker } from '@/components/PlanetaryTicker';
import { Footer } from '@/components/Footer';
import { Star, Moon, Calendar, Sparkles, ArrowRight } from 'lucide-react';
import { motion } from 'framer-motion';

export default function Landing() {
  return (
    <div className="min-h-screen bg-background text-foreground overflow-x-hidden">
      <CosmicBackground />
      
      {/* Navigation */}
      <nav className="fixed top-0 w-full z-50 bg-space-dark/80 backdrop-blur-sm border-b border-mystical-mid/20">
        <div className="max-w-7xl mx-auto px-6">
          <div className="flex items-center justify-between h-16">
            <div className="flex items-center gap-2">
              <div className="p-2 rounded-full bg-gradient-mystical">
                <Star className="h-6 w-6 text-white" />
              </div>
              <span className="text-xl font-playfair font-bold text-foreground">
                Celestial Sage
              </span>
            </div>

            <div className="flex items-center gap-4">
              <Link to="/login">
                <Button variant="ghost" className="text-foreground hover:text-celestial-mid">
                  Sign In
                </Button>
              </Link>
              <Link to="/signup">
                <Button className="bg-celestial-mid hover:bg-celestial-bright text-space-void">
                  Get Started
                </Button>
              </Link>
            </div>
          </div>
        </div>
      </nav>

      {/* Hero Section */}
      <main className="pt-16">
        <section className="relative min-h-screen flex items-center justify-center px-6">
          {/* Gradient Overlay */}
          <div className="absolute inset-0 bg-gradient-to-b from-space-void/20 via-space-dark/40 to-space-void/60 z-10" />
          
          <div className="relative z-20 max-w-4xl mx-auto text-center">
            <motion.div
              initial={{ opacity: 0, y: 30 }}
              animate={{ opacity: 1, y: 0 }}
              transition={{ duration: 0.8 }}
              className="space-y-8"
            >
              <h1 className="text-4xl md:text-6xl lg:text-7xl font-playfair font-bold">
                <span className="bg-gradient-mystical bg-clip-text text-transparent">
                  Discover Your
                </span>
                <br />
                <span className="text-celestial-bright">Cosmic Journey</span>
              </h1>
              
              <p className="text-lg md:text-xl text-muted-foreground max-w-2xl mx-auto leading-relaxed">
                Unlock the secrets of the universe with personalized astrology readings, 
                birth chart analysis, and cosmic guidance tailored just for you.
              </p>
              
              {/* Voice Search */}
              <div className="max-w-2xl mx-auto">
                <VoiceSearch placeholder="Ask me anything about astrology..." />
              </div>
              
              {/* CTA Buttons */}
              <div className="flex flex-col sm:flex-row gap-4 justify-center items-center">
                <Link to="/signup">
                  <Button 
                    size="lg" 
                    className="bg-celestial-mid hover:bg-celestial-bright text-space-void px-8 py-6 text-lg animate-pulse-glow"
                  >
                    <Sparkles className="mr-2 h-5 w-5" />
                    Get Free Birth Chart
                  </Button>
                </Link>
                
                <Link to="/signup">
                  <Button 
                    size="lg" 
                    variant="outline"
                    className="border-mystical-mid text-mystical-bright hover:bg-mystical-mid/10 px-8 py-6 text-lg"
                  >
                    <Calendar className="mr-2 h-5 w-5" />
                    Book Consultation
                    <ArrowRight className="ml-2 h-4 w-4" />
                  </Button>
                </Link>
              </div>
            </motion.div>
          </div>
        </section>

        {/* Planetary Ticker */}
        <PlanetaryTicker />

        {/* Features Section */}
        <section className="py-20 px-6 bg-space-dark/50">
          <div className="max-w-6xl mx-auto">
            <motion.div
              initial={{ opacity: 0, y: 30 }}
              whileInView={{ opacity: 1, y: 0 }}
              transition={{ duration: 0.8 }}
              className="text-center mb-16"
            >
              <h2 className="text-3xl md:text-4xl font-playfair font-bold text-foreground mb-4">
                Your Cosmic Toolkit
              </h2>
              <p className="text-muted-foreground text-lg max-w-2xl mx-auto">
                Explore the universe within you with our comprehensive astrology tools
              </p>
            </motion.div>

            <div className="grid md:grid-cols-3 gap-8">
              {[
                {
                  icon: <Moon className="h-8 w-8" />,
                  title: "Birth Chart Analysis",
                  description: "Get detailed insights into your personality, strengths, and life path through your natal chart."
                },
                {
                  icon: <Star className="h-8 w-8" />,
                  title: "Daily Cosmic Weather",
                  description: "Stay aligned with cosmic energies and planetary influences affecting your day."
                },
                {
                  icon: <Sparkles className="h-8 w-8" />,
                  title: "Personalized Guidance",
                  description: "Receive tailored advice and recommendations based on your unique astrological profile."
                }
              ].map((feature, index) => (
                <motion.div
                  key={index}
                  initial={{ opacity: 0, y: 30 }}
                  whileInView={{ opacity: 1, y: 0 }}
                  transition={{ duration: 0.8, delay: index * 0.2 }}
                  className="p-8 rounded-lg bg-card/50 border border-mystical-mid/20 hover:border-mystical-mid/40 transition-colors"
                >
                  <div className="text-celestial-mid mb-4">
                    {feature.icon}
                  </div>
                  <h3 className="text-xl font-semibold text-foreground mb-3">
                    {feature.title}
                  </h3>
                  <p className="text-muted-foreground">
                    {feature.description}
                  </p>
                </motion.div>
              ))}
            </div>
          </div>
        </section>

        {/* CTA Section */}
        <section className="py-20 px-6 bg-gradient-cosmic">
          <div className="max-w-4xl mx-auto text-center">
            <motion.div
              initial={{ opacity: 0, y: 30 }}
              whileInView={{ opacity: 1, y: 0 }}
              transition={{ duration: 0.8 }}
              className="space-y-8"
            >
              <h2 className="text-3xl md:text-4xl font-playfair font-bold text-white">
                Begin Your Cosmic Journey Today
              </h2>
              <p className="text-white/80 text-lg max-w-2xl mx-auto">
                Join thousands of seekers who have discovered their true potential through the wisdom of the stars.
              </p>
              <Link to="/signup">
                <Button 
                  size="lg" 
                  className="bg-white text-space-void hover:bg-white/90 px-8 py-6 text-lg"
                >
                  <Star className="mr-2 h-5 w-5" />
                  Start Your Reading
                </Button>
              </Link>
            </motion.div>
          </div>
        </section>
      </main>

      <Footer />
    </div>
  );
}