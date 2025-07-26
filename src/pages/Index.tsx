import { motion } from 'framer-motion';
import { CosmicBackground } from '../components/CosmicBackground';
import { Navigation } from '../components/Navigation';
import { Footer } from '../components/Footer';
import { VoiceSearch } from '../components/VoiceSearch';
import { PlanetaryTicker } from '../components/PlanetaryTicker';
import { CosmicWeatherCard } from '../components/CosmicWeatherCard';
import { QuickBirthChart } from '../components/QuickBirthChart';
import { LiveConsultation } from '../components/LiveConsultation';
import { Button } from '../components/ui/button';
import { Card } from '../components/ui/card';
import { Badge } from '../components/ui/badge';
import { 
  Star, 
  Moon, 
  Sparkles, 
  Calendar, 
  Heart, 
  TrendingUp,
  Users,
  Quote,
  ChevronRight,
  Zap
} from 'lucide-react';
import cosmicHeroBg from '../assets/cosmic-hero-bg.jpg';

const Index = () => {
  const handleVoiceSearch = (query: string) => {
    console.log('Voice search query:', query);
    // Handle search logic here
  };

  const testimonials = [
    {
      name: "Sarah M.",
      rating: 5,
      text: "Aurora's insights transformed my understanding of my life path. Absolutely life-changing!",
      verified: true
    },
    {
      name: "Michael R.",
      rating: 5,
      text: "The most accurate reading I've ever had. Everything resonated perfectly with my situation.",
      verified: true
    },
    {
      name: "Emma L.",
      rating: 5,
      text: "Her guidance during my Saturn return was invaluable. Couldn't have navigated it without her.",
      verified: true
    }
  ];

  const additionalTools = [
    {
      title: "Astrological Time Machine",
      description: "Explore how planetary positions affected historical events and your past",
      icon: <Calendar className="h-6 w-6" />,
      color: "mystical-mid"
    },
    {
      title: "Mood-Transit Correlation",
      description: "Track how current transits influence your daily emotional patterns",
      icon: <Heart className="h-6 w-6" />,
      color: "celestial-mid"
    },
    {
      title: "Family Astrology Glimpse",
      description: "Discover cosmic connections and compatibility within your family",
      icon: <Users className="h-6 w-6" />,
      color: "cosmic-bright"
    }
  ];

  return (
    <div className="min-h-screen bg-gradient-cosmic relative overflow-hidden">
      <Navigation />
      <CosmicBackground />
      
      {/* Hero Section */}
      <section id="home" className="relative z-10 min-h-screen flex items-center justify-center pt-16">
        <div 
          className="absolute inset-0 opacity-20"
          style={{
            backgroundImage: `url(${cosmicHeroBg})`,
            backgroundSize: 'cover',
            backgroundPosition: 'center',
            backgroundRepeat: 'no-repeat'
          }}
        />
        
        <div className="relative z-20 max-w-6xl mx-auto px-6 text-center">
          <motion.div
            initial={{ opacity: 0, y: 30 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ duration: 0.8 }}
            className="mb-8"
          >
            <div className="flex items-center justify-center gap-2 mb-4">
              <Sparkles className="h-8 w-8 text-celestial-mid animate-twinkle" />
              <h1 className="text-5xl md:text-7xl font-playfair font-bold text-foreground">
                Celestial Sage
              </h1>
              <Moon className="h-8 w-8 text-mystical-bright animate-float" />
            </div>
            
            <p className="text-xl md:text-2xl text-muted-foreground max-w-3xl mx-auto mb-8">
              Unlock the wisdom of the cosmos with personalized astrological guidance. 
              Discover your cosmic blueprint and navigate life's journey with celestial insight.
            </p>

            <div className="flex flex-wrap items-center justify-center gap-4 mb-12">
              <Badge className="bg-celestial-mid/20 text-celestial-mid border-celestial-mid/30 px-4 py-2">
                <Star className="h-4 w-4 mr-2" />
                5-Star Master Astrologer
              </Badge>
              <Badge className="bg-mystical-mid/20 text-mystical-mid border-mystical-mid/30 px-4 py-2">
                <TrendingUp className="h-4 w-4 mr-2" />
                3,200+ Successful Readings
              </Badge>
              <Badge className="bg-cosmic-bright/20 text-cosmic-bright border-cosmic-bright/30 px-4 py-2">
                <Zap className="h-4 w-4 mr-2" />
                15+ Years Experience
              </Badge>
            </div>
          </motion.div>

          <motion.div
            initial={{ opacity: 0, y: 20 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ duration: 0.8, delay: 0.3 }}
            className="mb-12"
          >
            <VoiceSearch onSearch={handleVoiceSearch} />
          </motion.div>

          <motion.div
            initial={{ opacity: 0, y: 20 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ duration: 0.8, delay: 0.5 }}
          >
            <PlanetaryTicker />
          </motion.div>
        </div>
      </section>

      {/* Interactive Dashboard */}
      <section className="relative z-10 py-20 px-6">
        <div className="max-w-7xl mx-auto">
          <motion.div
            initial={{ opacity: 0, y: 30 }}
            whileInView={{ opacity: 1, y: 0 }}
            viewport={{ once: true }}
            transition={{ duration: 0.8 }}
            className="text-center mb-16"
          >
            <h2 className="text-4xl md:text-5xl font-playfair font-bold text-foreground mb-6">
              Your Cosmic Dashboard
            </h2>
            <p className="text-xl text-muted-foreground max-w-3xl mx-auto">
              Real-time cosmic insights, personalized guidance, and powerful tools 
              to help you align with the universe's rhythm.
            </p>
          </motion.div>

          <div className="grid grid-cols-1 lg:grid-cols-2 gap-8 mb-16">
            <motion.div
              initial={{ opacity: 0, x: -30 }}
              whileInView={{ opacity: 1, x: 0 }}
              viewport={{ once: true }}
              transition={{ duration: 0.8, delay: 0.2 }}
            >
              <CosmicWeatherCard />
            </motion.div>
            
            <motion.div
              initial={{ opacity: 0, x: 30 }}
              whileInView={{ opacity: 1, x: 0 }}
              viewport={{ once: true }}
              transition={{ duration: 0.8, delay: 0.4 }}
            >
              <LiveConsultation />
            </motion.div>
          </div>

          <motion.div
            initial={{ opacity: 0, y: 30 }}
            whileInView={{ opacity: 1, y: 0 }}
            viewport={{ once: true }}
            transition={{ duration: 0.8, delay: 0.6 }}
            className="mb-16"
          >
            <QuickBirthChart />
          </motion.div>

          {/* Additional Tools */}
          <motion.div
            initial={{ opacity: 0, y: 30 }}
            whileInView={{ opacity: 1, y: 0 }}
            viewport={{ once: true }}
            transition={{ duration: 0.8, delay: 0.8 }}
          >
            <h3 className="text-3xl font-playfair font-bold text-foreground text-center mb-8">
              Advanced Cosmic Tools
            </h3>
            <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
              {additionalTools.map((tool, index) => (
                <motion.div
                  key={tool.title}
                  initial={{ opacity: 0, y: 20 }}
                  whileInView={{ opacity: 1, y: 0 }}
                  viewport={{ once: true }}
                  transition={{ duration: 0.6, delay: index * 0.1 }}
                >
                  <Card className="bg-space-dark/80 backdrop-blur-sm border-mystical-mid/20 p-6 shadow-cosmic hover:shadow-glow transition-all duration-300 group cursor-pointer">
                    <div className="flex items-start gap-4">
                      <div className={`p-3 rounded-full bg-${tool.color}/20 text-${tool.color} group-hover:scale-110 transition-transform duration-300`}>
                        {tool.icon}
                      </div>
                      <div className="flex-1">
                        <h4 className="font-playfair font-semibold text-foreground mb-2 group-hover:text-celestial-mid transition-colors">
                          {tool.title}
                        </h4>
                        <p className="text-sm text-muted-foreground mb-3">
                          {tool.description}
                        </p>
                        <div className="flex items-center text-sm text-mystical-bright group-hover:text-mystical-mid transition-colors">
                          <span>Coming Soon</span>
                          <ChevronRight className="h-4 w-4 ml-1 group-hover:translate-x-1 transition-transform" />
                        </div>
                      </div>
                    </div>
                  </Card>
                </motion.div>
              ))}
            </div>
          </motion.div>
        </div>
      </section>

      {/* Success Stories */}
      <section className="relative z-10 py-20 px-6 bg-space-dark/50">
        <div className="max-w-6xl mx-auto">
          <motion.div
            initial={{ opacity: 0, y: 30 }}
            whileInView={{ opacity: 1, y: 0 }}
            viewport={{ once: true }}
            transition={{ duration: 0.8 }}
            className="text-center mb-16"
          >
            <h2 className="text-4xl md:text-5xl font-playfair font-bold text-foreground mb-6">
              Cosmic Transformations
            </h2>
            <p className="text-xl text-muted-foreground max-w-3xl mx-auto">
              Real stories from souls who found clarity and direction through celestial guidance.
            </p>
          </motion.div>

          <div className="grid grid-cols-1 md:grid-cols-3 gap-8">
            {testimonials.map((testimonial, index) => (
              <motion.div
                key={index}
                initial={{ opacity: 0, y: 30 }}
                whileInView={{ opacity: 1, y: 0 }}
                viewport={{ once: true }}
                transition={{ duration: 0.8, delay: index * 0.2 }}
              >
                <Card className="bg-space-dark/80 backdrop-blur-sm border-mystical-mid/20 p-6 shadow-cosmic h-full">
                  <div className="flex items-center gap-1 mb-4">
                    {Array.from({ length: testimonial.rating }, (_, i) => (
                      <Star key={i} className="h-4 w-4 fill-celestial-mid text-celestial-mid" />
                    ))}
                  </div>
                  
                  <div className="mb-4">
                    <Quote className="h-6 w-6 text-mystical-mid mb-2" />
                    <p className="text-foreground italic">"{testimonial.text}"</p>
                  </div>
                  
                  <div className="flex items-center justify-between">
                    <span className="font-medium text-foreground">{testimonial.name}</span>
                    {testimonial.verified && (
                      <Badge className="bg-green-500/20 text-green-400 border-green-500/30">
                        Verified
                      </Badge>
                    )}
                  </div>
                </Card>
              </motion.div>
            ))}
          </div>

          <motion.div
            initial={{ opacity: 0, y: 20 }}
            whileInView={{ opacity: 1, y: 0 }}
            viewport={{ once: true }}
            transition={{ duration: 0.8, delay: 0.8 }}
            className="text-center mt-12"
          >
            <Button className="bg-gradient-celestial hover:shadow-glow text-space-void px-8 py-3 text-lg">
              <Sparkles className="h-5 w-5 mr-2" />
              Begin Your Cosmic Journey
            </Button>
          </motion.div>
        </div>
      </section>

      {/* Floating CTA */}
      <motion.div
        initial={{ opacity: 0, scale: 0.8 }}
        animate={{ opacity: 1, scale: 1 }}
        transition={{ duration: 0.8, delay: 2 }}
        className="fixed bottom-6 right-6 z-50"
      >
        <Button className="bg-mystical-mid hover:bg-mystical-bright animate-pulse-glow rounded-full p-4 shadow-cosmic">
          <Star className="h-6 w-6" />
        </Button>
      </motion.div>

      <Footer />
    </div>
  );
};

export default Index;
