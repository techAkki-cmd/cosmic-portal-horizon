import { Star, Moon, Heart, Mail, Phone, MapPin } from 'lucide-react';
import { Button } from './ui/button';
import { Input } from './ui/input';

export function Footer() {
  return (
    <footer className="bg-space-void border-t border-mystical-mid/20">
      <div className="max-w-7xl mx-auto px-6 py-16">
        <div className="grid grid-cols-1 md:grid-cols-4 gap-8">
          {/* Brand */}
          <div className="space-y-4">
            <div className="flex items-center gap-2">
              <div className="p-2 rounded-full bg-gradient-mystical">
                <Star className="h-6 w-6 text-white" />
              </div>
              <span className="text-xl font-playfair font-bold text-foreground">
                Celestial Sage
              </span>
            </div>
            <p className="text-muted-foreground text-sm">
              Guiding souls through cosmic wisdom and celestial insight. 
              Discover your path among the stars.
            </p>
            <div className="flex items-center gap-2 text-sm text-mystical-bright">
              <Heart className="h-4 w-4" />
              <span>Made with cosmic love</span>
            </div>
          </div>

          {/* Services */}
          <div className="space-y-4">
            <h3 className="text-lg font-playfair font-semibold text-foreground">
              Services
            </h3>
            <ul className="space-y-2 text-sm text-muted-foreground">
              <li>
                <a href="#" className="hover:text-celestial-mid transition-colors">
                  Birth Chart Reading
                </a>
              </li>
              <li>
                <a href="#" className="hover:text-celestial-mid transition-colors">
                  Transit Analysis
                </a>
              </li>
              <li>
                <a href="#" className="hover:text-celestial-mid transition-colors">
                  Relationship Compatibility
                </a>
              </li>
              <li>
                <a href="#" className="hover:text-celestial-mid transition-colors">
                  Career Guidance
                </a>
              </li>
              <li>
                <a href="#" className="hover:text-celestial-mid transition-colors">
                  Spiritual Counseling
                </a>
              </li>
            </ul>
          </div>

          {/* Contact */}
          <div className="space-y-4">
            <h3 className="text-lg font-playfair font-semibold text-foreground">
              Contact
            </h3>
            <div className="space-y-3 text-sm text-muted-foreground">
              <div className="flex items-center gap-2">
                <Mail className="h-4 w-4 text-celestial-mid" />
                <span>aurora@celestialsage.com</span>
              </div>
              <div className="flex items-center gap-2">
                <Phone className="h-4 w-4 text-celestial-mid" />
                <span>+1 (555) 123-STAR</span>
              </div>
              <div className="flex items-center gap-2">
                <MapPin className="h-4 w-4 text-celestial-mid" />
                <span>Available Worldwide</span>
              </div>
            </div>
          </div>

          {/* Newsletter */}
          <div className="space-y-4">
            <h3 className="text-lg font-playfair font-semibold text-foreground">
              Cosmic Updates
            </h3>
            <p className="text-sm text-muted-foreground">
              Subscribe for weekly cosmic insights and celestial guidance.
            </p>
            <div className="space-y-2">
              <Input
                type="email"
                placeholder="Enter your email"
                className="bg-space-dark/50 border-mystical-mid/30"
              />
              <Button className="w-full bg-celestial-mid hover:bg-celestial-bright text-space-void">
                Subscribe
              </Button>
            </div>
          </div>
        </div>

        <div className="mt-12 pt-8 border-t border-mystical-mid/20">
          <div className="flex flex-col md:flex-row items-center justify-between gap-4">
            <div className="text-sm text-muted-foreground">
              © 2024 Celestial Sage. All rights reserved.
            </div>
            <div className="flex items-center gap-2">
              <Moon className="h-4 w-4 text-mystical-bright animate-float" />
              <span className="text-sm text-mystical-bright">
                The stars align for those who seek
              </span>
            </div>
          </div>
        </div>
      </div>
    </footer>
  );
}