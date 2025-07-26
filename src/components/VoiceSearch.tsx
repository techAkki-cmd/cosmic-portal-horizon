import { useState, useRef, useEffect } from 'react';
import { Button } from './ui/button';
import { Input } from './ui/input';
import { Mic, MicOff, Search } from 'lucide-react';
import { motion } from 'framer-motion';

interface VoiceSearchProps {
  onSearch: (query: string) => void;
}

export function VoiceSearch({ onSearch }: VoiceSearchProps) {
  const [isListening, setIsListening] = useState(false);
  const [searchQuery, setSearchQuery] = useState('');
  const [transcript, setTranscript] = useState('');
  const recognitionRef = useRef<SpeechRecognition | null>(null);

  useEffect(() => {
    if ('webkitSpeechRecognition' in window || 'SpeechRecognition' in window) {
      const SpeechRecognition = (window as any).SpeechRecognition || (window as any).webkitSpeechRecognition;
      recognitionRef.current = new SpeechRecognition();
      
      if (recognitionRef.current) {
        recognitionRef.current.continuous = false;
        recognitionRef.current.interimResults = true;
        recognitionRef.current.lang = 'en-US';

        recognitionRef.current.onresult = (event) => {
          const result = event.results[event.results.length - 1];
          const transcript = result[0].transcript;
          
          if (result.isFinal) {
            setSearchQuery(transcript);
            setTranscript('');
            onSearch(transcript);
          } else {
            setTranscript(transcript);
          }
        };

        recognitionRef.current.onend = () => {
          setIsListening(false);
        };
      }
    }
  }, [onSearch]);

  const toggleListening = () => {
    if (recognitionRef.current) {
      if (isListening) {
        recognitionRef.current.stop();
        setIsListening(false);
      } else {
        recognitionRef.current.start();
        setIsListening(true);
      }
    }
  };

  const handleSearch = () => {
    if (searchQuery.trim()) {
      onSearch(searchQuery);
    }
  };

  return (
    <div className="relative w-full max-w-2xl mx-auto">
      <div className="relative flex gap-3">
        <div className="flex-1 relative">
          <Input
            type="text"
            placeholder="Ask about your cosmic journey..."
            value={transcript || searchQuery}
            onChange={(e) => setSearchQuery(e.target.value)}
            onKeyPress={(e) => e.key === 'Enter' && handleSearch()}
            className="pr-12 bg-space-dark/50 border-mystical-mid/30 backdrop-blur-sm text-foreground placeholder:text-muted-foreground"
          />
          <Button
            onClick={handleSearch}
            size="sm"
            className="absolute right-1 top-1/2 -translate-y-1/2 h-8 w-8 bg-celestial-mid hover:bg-celestial-bright text-space-void"
          >
            <Search className="h-4 w-4" />
          </Button>
        </div>
        
        <Button
          onClick={toggleListening}
          variant={isListening ? "default" : "secondary"}
          size="lg"
          className={`relative ${
            isListening 
              ? "bg-mystical-mid hover:bg-mystical-bright animate-pulse-glow" 
              : "bg-secondary hover:bg-secondary/80"
          }`}
        >
          {isListening ? <MicOff className="h-5 w-5" /> : <Mic className="h-5 w-5" />}
          
          {isListening && (
            <motion.div
              className="absolute inset-0 rounded-md border-2 border-mystical-bright/50"
              animate={{
                scale: [1, 1.1, 1],
                opacity: [0.5, 0.8, 0.5],
              }}
              transition={{
                duration: 1.5,
                repeat: Infinity,
                ease: "easeInOut",
              }}
            />
          )}
        </Button>
      </div>
      
      {isListening && (
        <motion.div
          initial={{ opacity: 0, y: 10 }}
          animate={{ opacity: 1, y: 0 }}
          className="absolute top-full mt-2 w-full text-center text-sm text-mystical-bright"
        >
          Listening... Speak your cosmic question
        </motion.div>
      )}
    </div>
  );
}

declare global {
  interface Window {
    SpeechRecognition: any;
    webkitSpeechRecognition: any;
  }
}

interface SpeechRecognition extends EventTarget {
  continuous: boolean;
  interimResults: boolean;
  lang: string;
  onresult: ((event: any) => void) | null;
  onend: (() => void) | null;
  start(): void;
  stop(): void;
}