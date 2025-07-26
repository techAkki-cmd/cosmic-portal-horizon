import { useState } from 'react';
import { Card } from './ui/card';
import { Button } from './ui/button';
import { Input } from './ui/input';
import { Label } from './ui/label';
import { Calendar, MapPin, Clock, Download, Share2 } from 'lucide-react';
import { motion } from 'framer-motion';
import birthChartWheel from '../assets/birth-chart-wheel.jpg';

interface BirthData {
  name: string;
  date: string;
  time: string;
  location: string;
}

export function QuickBirthChart() {
  const [step, setStep] = useState(1);
  const [isGenerating, setIsGenerating] = useState(false);
  const [birthData, setBirthData] = useState<BirthData>({
    name: '',
    date: '',
    time: '',
    location: ''
  });

  const handleInputChange = (field: keyof BirthData, value: string) => {
    setBirthData(prev => ({ ...prev, [field]: value }));
  };

  const handleNext = () => {
    if (step < 3) {
      setStep(step + 1);
    } else {
      generateChart();
    }
  };

  const generateChart = async () => {
    setIsGenerating(true);
    // Simulate chart generation
    setTimeout(() => {
      setIsGenerating(false);
      setStep(4);
    }, 3000);
  };

  const isStepValid = () => {
    switch (step) {
      case 1:
        return birthData.name.trim() !== '' && birthData.date !== '';
      case 2:
        return birthData.time !== '';
      case 3:
        return birthData.location.trim() !== '';
      default:
        return true;
    }
  };

  return (
    <Card className="bg-space-dark/80 backdrop-blur-sm border-mystical-mid/20 p-6 shadow-cosmic">
      <div className="flex items-center gap-3 mb-6">
        <div className="p-2 rounded-full bg-gradient-celestial">
          <Calendar className="h-6 w-6 text-white" />
        </div>
        <div>
          <h3 className="text-xl font-playfair font-semibold text-foreground">
            Quick Birth Chart Generator
          </h3>
          <p className="text-sm text-muted-foreground">
            Create your personalized cosmic blueprint in minutes
          </p>
        </div>
      </div>

      {step < 4 && (
        <div className="mb-6">
          <div className="flex items-center justify-between mb-2">
            <span className="text-sm text-muted-foreground">Progress</span>
            <span className="text-sm text-celestial-mid">{step}/3</span>
          </div>
          <div className="w-full bg-space-medium rounded-full h-2">
            <motion.div
              className="bg-gradient-celestial h-2 rounded-full"
              initial={{ width: "0%" }}
              animate={{ width: `${(step / 3) * 100}%` }}
              transition={{ duration: 0.3 }}
            />
          </div>
        </div>
      )}

      <AnimatePresence mode="wait">
        {step === 1 && (
          <motion.div
            key="step1"
            initial={{ opacity: 0, x: 20 }}
            animate={{ opacity: 1, x: 0 }}
            exit={{ opacity: 0, x: -20 }}
            className="space-y-4"
          >
            <div>
              <Label htmlFor="name" className="text-foreground">Full Name</Label>
              <Input
                id="name"
                value={birthData.name}
                onChange={(e) => handleInputChange('name', e.target.value)}
                placeholder="Enter your full name"
                className="bg-space-dark/50 border-mystical-mid/30"
              />
            </div>
            <div>
              <Label htmlFor="date" className="text-foreground">Birth Date</Label>
              <Input
                id="date"
                type="date"
                value={birthData.date}
                onChange={(e) => handleInputChange('date', e.target.value)}
                className="bg-space-dark/50 border-mystical-mid/30"
              />
            </div>
          </motion.div>
        )}

        {step === 2 && (
          <motion.div
            key="step2"
            initial={{ opacity: 0, x: 20 }}
            animate={{ opacity: 1, x: 0 }}
            exit={{ opacity: 0, x: -20 }}
            className="space-y-4"
          >
            <div className="flex items-center gap-2 mb-4">
              <Clock className="h-5 w-5 text-celestial-mid" />
              <span className="text-foreground font-medium">Birth Time</span>
            </div>
            <div>
              <Label htmlFor="time" className="text-foreground">Exact Time of Birth</Label>
              <Input
                id="time"
                type="time"
                value={birthData.time}
                onChange={(e) => handleInputChange('time', e.target.value)}
                className="bg-space-dark/50 border-mystical-mid/30"
              />
              <p className="text-xs text-muted-foreground mt-1">
                Precise time is crucial for accurate chart calculation
              </p>
            </div>
          </motion.div>
        )}

        {step === 3 && (
          <motion.div
            key="step3"
            initial={{ opacity: 0, x: 20 }}
            animate={{ opacity: 1, x: 0 }}
            exit={{ opacity: 0, x: -20 }}
            className="space-y-4"
          >
            <div className="flex items-center gap-2 mb-4">
              <MapPin className="h-5 w-5 text-celestial-mid" />
              <span className="text-foreground font-medium">Birth Location</span>
            </div>
            <div>
              <Label htmlFor="location" className="text-foreground">City, State/Country</Label>
              <Input
                id="location"
                value={birthData.location}
                onChange={(e) => handleInputChange('location', e.target.value)}
                placeholder="e.g., New York, NY, USA"
                className="bg-space-dark/50 border-mystical-mid/30"
              />
              <p className="text-xs text-muted-foreground mt-1">
                Enter your exact birth location for precise calculations
              </p>
            </div>
          </motion.div>
        )}

        {step === 4 && (
          <motion.div
            key="step4"
            initial={{ opacity: 0, scale: 0.95 }}
            animate={{ opacity: 1, scale: 1 }}
            className="text-center space-y-6"
          >
            <div className="relative">
              <img
                src={birthChartWheel}
                alt="Generated Birth Chart"
                className="w-full max-w-sm mx-auto rounded-lg border border-mystical-mid/20"
              />
              <div className="absolute inset-0 bg-gradient-to-t from-mystical-deep/20 to-transparent rounded-lg" />
            </div>
            
            <div>
              <h4 className="text-xl font-playfair font-semibold text-foreground mb-2">
                Your Cosmic Blueprint is Ready!
              </h4>
              <p className="text-muted-foreground">
                Chart generated for {birthData.name}
              </p>
            </div>

            <div className="flex gap-3 justify-center">
              <Button className="bg-celestial-mid hover:bg-celestial-bright text-space-void">
                <Download className="h-4 w-4 mr-2" />
                Download PDF
              </Button>
              <Button variant="secondary" className="bg-secondary/50 hover:bg-secondary/70">
                <Share2 className="h-4 w-4 mr-2" />
                Share Chart
              </Button>
            </div>
          </motion.div>
        )}
      </AnimatePresence>

      {step < 4 && !isGenerating && (
        <div className="flex gap-3 mt-6">
          {step > 1 && (
            <Button
              onClick={() => setStep(step - 1)}
              variant="secondary"
              className="flex-1"
            >
              Previous
            </Button>
          )}
          <Button
            onClick={handleNext}
            disabled={!isStepValid()}
            className="flex-1 bg-celestial-mid hover:bg-celestial-bright text-space-void disabled:opacity-50"
          >
            {step === 3 ? 'Generate Chart' : 'Next'}
          </Button>
        </div>
      )}

      {isGenerating && (
        <motion.div
          initial={{ opacity: 0 }}
          animate={{ opacity: 1 }}
          className="text-center py-8"
        >
          <div className="relative">
            <div className="w-16 h-16 mx-auto mb-4 rounded-full border-4 border-mystical-mid/20 border-t-mystical-mid animate-spin" />
            <div className="absolute inset-0 w-16 h-16 mx-auto rounded-full bg-mystical-mid/20 animate-pulse" />
          </div>
          <h4 className="text-lg font-medium text-foreground mb-2">Calculating Your Chart...</h4>
          <p className="text-sm text-muted-foreground">
            Analyzing planetary positions and cosmic influences
          </p>
        </motion.div>
      )}
    </Card>
  );
}

function AnimatePresence({ mode, children }: { mode?: string; children: React.ReactNode }) {
  return <div>{children}</div>;
}