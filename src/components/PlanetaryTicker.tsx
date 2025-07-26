import { useEffect, useState } from 'react';
import { motion } from 'framer-motion';
import { Moon, Sun, Star } from 'lucide-react';

interface PlanetaryData {
  name: string;
  sign: string;
  degree: string;
  aspect: string;
  icon: React.ReactNode;
}

export function PlanetaryTicker() {
  const [planetaryData] = useState<PlanetaryData[]>([
    { name: "Sun", sign: "Capricorn", degree: "0°12'", aspect: "Trine Moon", icon: <Sun className="h-4 w-4" /> },
    { name: "Moon", sign: "Taurus", degree: "15°33'", aspect: "Sextile Venus", icon: <Moon className="h-4 w-4" /> },
    { name: "Mercury", sign: "Sagittarius", degree: "28°45'", aspect: "Square Mars", icon: <Star className="h-3 w-3" /> },
    { name: "Venus", sign: "Aquarius", degree: "8°21'", aspect: "Trine Jupiter", icon: <Star className="h-3 w-3" /> },
    { name: "Mars", sign: "Gemini", degree: "22°17'", aspect: "Opposition Saturn", icon: <Star className="h-3 w-3" /> },
    { name: "Jupiter", sign: "Gemini", degree: "19°54'", aspect: "Conjunct Mars", icon: <Star className="h-4 w-4" /> },
  ]);

  const [currentTime, setCurrentTime] = useState(new Date());
  const [moonPhase, setMoonPhase] = useState("Waxing Crescent");

  useEffect(() => {
    const timer = setInterval(() => {
      setCurrentTime(new Date());
    }, 1000);

    return () => clearInterval(timer);
  }, []);

  return (
    <div className="bg-space-dark/80 backdrop-blur-sm border border-mystical-mid/20 rounded-lg p-4 overflow-hidden">
      <div className="flex items-center justify-between mb-3">
        <h3 className="text-lg font-playfair font-semibold text-celestial-mid">
          Current Cosmic Energies
        </h3>
        <div className="text-sm text-muted-foreground">
          {currentTime.toLocaleString()}
        </div>
      </div>
      
      <div className="relative">
        <motion.div
          className="flex gap-6"
          animate={{ x: ["0%", "-50%"] }}
          transition={{
            duration: 60,
            repeat: Infinity,
            ease: "linear",
          }}
        >
          {[...planetaryData, ...planetaryData].map((planet, index) => (
            <div
              key={index}
              className="flex items-center gap-3 whitespace-nowrap bg-card/30 rounded-md p-3 border border-mystical-mid/10"
            >
              <div className="text-celestial-mid">{planet.icon}</div>
              <div>
                <div className="font-semibold text-foreground">{planet.name}</div>
                <div className="text-sm text-muted-foreground">
                  {planet.sign} {planet.degree}
                </div>
                <div className="text-xs text-mystical-bright">{planet.aspect}</div>
              </div>
            </div>
          ))}
        </motion.div>
      </div>
      
      <div className="mt-4 pt-3 border-t border-mystical-mid/20">
        <div className="flex items-center justify-between text-sm">
          <div className="flex items-center gap-2">
            <Moon className="h-4 w-4 text-celestial-mid" />
            <span className="text-foreground">Moon Phase: {moonPhase}</span>
          </div>
          <div className="text-mystical-bright">Mercury: Direct</div>
        </div>
      </div>
    </div>
  );
}