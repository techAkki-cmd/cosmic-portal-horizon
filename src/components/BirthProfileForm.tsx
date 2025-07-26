// src/components/BirthProfileForm.tsx
import React, { useState } from 'react';
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Label } from '@/components/ui/label';
import { CosmicBackground } from '@/components/CosmicBackground';
import { authService } from '@/services/authService';
import { Calendar, MapPin, Clock, Star, Loader2, AlertCircle, Shield, Sparkles } from 'lucide-react';
import { motion } from 'framer-motion';

interface BirthProfileFormProps {
  onComplete: () => void;
}

interface FormData {
  birthDate: string;
  birthTime: string;
  birthLocation: string;
  birthLatitude: string;
  birthLongitude: string;
  timezone: string;
}

interface FormErrors {
  birthDate?: string;
  birthLocation?: string;
  general?: string;
}

export const BirthProfileForm: React.FC<BirthProfileFormProps> = ({ onComplete }) => {
  const [formData, setFormData] = useState<FormData>({
    birthDate: '',
    birthTime: '12:00',
    birthLocation: '',
    birthLatitude: '',
    birthLongitude: '',
    timezone: 'UTC'
  });

  const [errors, setErrors] = useState<FormErrors>({});
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [isGeocodingLocation, setIsGeocodingLocation] = useState(false);

  // Timezone options
  const timezoneOptions = [
    { value: 'UTC', label: 'UTC' },
    { value: 'America/New_York', label: 'Eastern Time (US)' },
    { value: 'America/Chicago', label: 'Central Time (US)' },
    { value: 'America/Denver', label: 'Mountain Time (US)' },
    { value: 'America/Los_Angeles', label: 'Pacific Time (US)' },
    { value: 'Europe/London', label: 'London' },
    { value: 'Europe/Paris', label: 'Paris' },
    { value: 'Europe/Berlin', label: 'Berlin' },
    { value: 'Asia/Tokyo', label: 'Tokyo' },
    { value: 'Asia/Shanghai', label: 'Shanghai' },
    { value: 'Asia/Kolkata', label: 'India Standard Time' },
    { value: 'Australia/Sydney', label: 'Sydney' },
    { value: 'Australia/Melbourne', label: 'Melbourne' },
  ];

  // Simple geocoding function (you can replace with a real API)
  const geocodeLocation = async (location: string) => {
    setIsGeocodingLocation(true);
    try {
      // This is a simplified example. In production, use a real geocoding service
      // like Google Maps Geocoding API, OpenCage, or Nominatim
      
      // For now, just some common cities as examples
      const commonLocations: Record<string, { lat: number; lng: number; timezone: string }> = {
        'new york, usa': { lat: 40.7128, lng: -74.0060, timezone: 'America/New_York' },
        'london, uk': { lat: 51.5074, lng: -0.1278, timezone: 'Europe/London' },
        'paris, france': { lat: 48.8566, lng: 2.3522, timezone: 'Europe/Paris' },
        'tokyo, japan': { lat: 35.6762, lng: 139.6503, timezone: 'Asia/Tokyo' },
        'mumbai, india': { lat: 19.0760, lng: 72.8777, timezone: 'Asia/Kolkata' },
        'delhi, india': { lat: 28.7041, lng: 77.1025, timezone: 'Asia/Kolkata' },
        'bangalore, india': { lat: 12.9716, lng: 77.5946, timezone: 'Asia/Kolkata' },
        'sydney, australia': { lat: -33.8688, lng: 151.2093, timezone: 'Australia/Sydney' },
        'los angeles, usa': { lat: 34.0522, lng: -118.2437, timezone: 'America/Los_Angeles' },
        'chicago, usa': { lat: 41.8781, lng: -87.6298, timezone: 'America/Chicago' },
      };

      const key = location.toLowerCase().trim();
      const coords = commonLocations[key];
      
      if (coords) {
        setFormData(prev => ({
          ...prev,
          birthLatitude: coords.lat.toString(),
          birthLongitude: coords.lng.toString(),
          timezone: coords.timezone
        }));
      }
    } catch (error) {
      console.error('Geocoding failed:', error);
    } finally {
      setIsGeocodingLocation(false);
    }
  };

  // Handle location change with auto-geocoding
  const handleLocationChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const location = e.target.value;
    setFormData(prev => ({ ...prev, birthLocation: location }));
    
    // Auto-geocode if location looks complete
    if (location.includes(',') && location.length > 5) {
      const timeoutId = setTimeout(() => {
        geocodeLocation(location);
      }, 1000); // Debounce for 1 second
      
      return () => clearTimeout(timeoutId);
    }
  };

  // Form validation
  const validateForm = (): boolean => {
    const newErrors: FormErrors = {};

    if (!formData.birthDate) {
      newErrors.birthDate = 'Birth date is required';
    }

    if (!formData.birthLocation.trim()) {
      newErrors.birthLocation = 'Birth location is required';
    }

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  // Handle form submission
  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    
    if (!validateForm()) {
      return;
    }

    setIsSubmitting(true);
    setErrors({});

    try {
      // Combine date and time
      const birthDateTime = `${formData.birthDate}T${formData.birthTime}:00`;
      
      // Prepare data for API
      const birthProfileData = {
        birthDateTime,
        birthLocation: formData.birthLocation.trim(),
        birthLatitude: formData.birthLatitude ? parseFloat(formData.birthLatitude) : null,
        birthLongitude: formData.birthLongitude ? parseFloat(formData.birthLongitude) : null,
        timezone: formData.timezone
      };

      // Call the auth service to update birth profile
      await authService.updateBirthProfile(birthProfileData);
      
      // Success! Call completion callback
      onComplete();
      
    } catch (error) {
      console.error('Failed to update birth profile:', error);
      setErrors({
        general: error instanceof Error 
          ? error.message 
          : 'Failed to update birth profile. Please try again.'
      });
    } finally {
      setIsSubmitting(false);
    }
  };

  return (
    <div className="min-h-screen bg-background text-foreground flex items-center justify-center p-6">
      <CosmicBackground />
      
      <motion.div
        initial={{ opacity: 0, scale: 0.95 }}
        animate={{ opacity: 1, scale: 1 }}
        transition={{ duration: 0.5 }}
        className="w-full max-w-2xl relative z-10"
      >
        <Card className="bg-card/90 backdrop-blur-sm border-mystical-mid/30 shadow-2xl">
          <CardHeader className="text-center pb-6">
            <motion.div
              initial={{ opacity: 0, y: -20 }}
              animate={{ opacity: 1, y: 0 }}
              transition={{ delay: 0.2 }}
              className="flex justify-center mb-4"
            >
              <div className="p-4 rounded-full bg-gradient-mystical">
                <Star className="h-12 w-12 text-white" />
              </div>
            </motion.div>
            
            <CardTitle className="text-3xl font-playfair bg-gradient-mystical bg-clip-text text-transparent">
              Complete Your Cosmic Profile
            </CardTitle>
            <CardDescription className="text-lg text-muted-foreground mt-2">
              Add your birth information to unlock personalized astrological insights tailored just for you
            </CardDescription>
          </CardHeader>
          
          <CardContent className="space-y-6">
            <form onSubmit={handleSubmit} className="space-y-6">
              
              {/* General Error */}
              {errors.general && (
                <motion.div
                  initial={{ opacity: 0, y: -10 }}
                  animate={{ opacity: 1, y: 0 }}
                  className="p-4 bg-red-500/10 border border-red-500/20 rounded-lg text-red-400 flex items-center gap-2"
                >
                  <AlertCircle className="h-4 w-4 flex-shrink-0" />
                  {errors.general}
                </motion.div>
              )}
              
              {/* Birth Date */}
              <div className="space-y-2">
                <Label htmlFor="birthDate" className="flex items-center gap-2 text-foreground">
                  <Calendar className="h-4 w-4 text-celestial-mid" />
                  Birth Date *
                </Label>
                <Input
                  id="birthDate"
                  type="date"
                  value={formData.birthDate}
                  onChange={(e) => setFormData(prev => ({ ...prev, birthDate: e.target.value }))}
                  className={`bg-input border-border ${errors.birthDate ? 'border-red-500' : ''}`}
                  required
                />
                {errors.birthDate && (
                  <p className="text-sm text-red-400">{errors.birthDate}</p>
                )}
              </div>
              
              {/* Birth Time */}
              <div className="space-y-2">
                <Label htmlFor="birthTime" className="flex items-center gap-2 text-foreground">
                  <Clock className="h-4 w-4 text-celestial-mid" />
                  Birth Time
                </Label>
                <Input
                  id="birthTime"
                  type="time"
                  value={formData.birthTime}
                  onChange={(e) => setFormData(prev => ({ ...prev, birthTime: e.target.value }))}
                  className="bg-input border-border"
                />
                <p className="text-xs text-muted-foreground">
                  If unknown, leave as noon (12:00) for best approximation
                </p>
              </div>
              
              {/* Birth Location */}
              <div className="space-y-2">
                <Label htmlFor="birthLocation" className="flex items-center gap-2 text-foreground">
                  <MapPin className="h-4 w-4 text-celestial-mid" />
                  Birth Location *
                  {isGeocodingLocation && (
                    <Loader2 className="h-3 w-3 animate-spin text-celestial-mid" />
                  )}
                </Label>
                <Input
                  id="birthLocation"
                  type="text"
                  placeholder="e.g., New York, USA or Mumbai, India"
                  value={formData.birthLocation}
                  onChange={handleLocationChange}
                  className={`bg-input border-border ${errors.birthLocation ? 'border-red-500' : ''}`}
                  required
                />
                {errors.birthLocation && (
                  <p className="text-sm text-red-400">{errors.birthLocation}</p>
                )}
                <p className="text-xs text-muted-foreground">
                  Enter city and country for automatic coordinate detection
                </p>
              </div>
              
              {/* Coordinates (Optional) */}
              <div className="grid grid-cols-2 gap-4">
                <div className="space-y-2">
                  <Label htmlFor="birthLatitude" className="text-sm text-foreground">
                    Latitude (optional)
                  </Label>
                  <Input
                    id="birthLatitude"
                    type="number"
                    step="any"
                    placeholder="e.g., 40.7128"
                    value={formData.birthLatitude}
                    onChange={(e) => setFormData(prev => ({ ...prev, birthLatitude: e.target.value }))}
                    className="bg-input border-border"
                  />
                </div>
                <div className="space-y-2">
                  <Label htmlFor="birthLongitude" className="text-sm text-foreground">
                    Longitude (optional)
                  </Label>
                  <Input
                    id="birthLongitude"
                    type="number"
                    step="any"
                    placeholder="e.g., -74.0060"
                    value={formData.birthLongitude}
                    onChange={(e) => setFormData(prev => ({ ...prev, birthLongitude: e.target.value }))}
                    className="bg-input border-border"
                  />
                </div>
              </div>
              
              {/* Timezone */}
              <div className="space-y-2">
                <Label htmlFor="timezone" className="text-sm text-foreground">
                  Timezone
                </Label>
                <select
                  id="timezone"
                  value={formData.timezone}
                  onChange={(e) => setFormData(prev => ({ ...prev, timezone: e.target.value }))}
                  className="w-full px-3 py-2 border border-border rounded-md bg-input text-foreground focus:outline-none focus:ring-2 focus:ring-celestial-mid"
                >
                  {timezoneOptions.map((option) => (
                    <option key={option.value} value={option.value}>
                      {option.label}
                    </option>
                  ))}
                </select>
              </div>
              
              {/* Submit Button */}
              <motion.div
                initial={{ opacity: 0, y: 20 }}
                animate={{ opacity: 1, y: 0 }}
                transition={{ delay: 0.3 }}
                className="pt-4"
              >
                <Button
                  type="submit"
                  disabled={isSubmitting}
                  className="w-full bg-gradient-mystical hover:opacity-90 text-white font-medium py-3 text-lg transition-all duration-300 disabled:opacity-50"
                >
                  {isSubmitting ? (
                    <>
                      <Loader2 className="mr-2 h-5 w-5 animate-spin" />
                      Calculating Your Cosmic Profile...
                    </>
                  ) : (
                    <>
                      <Star className="mr-2 h-5 w-5" />
                      Unlock Personalized Insights
                    </>
                  )}
                </Button>
              </motion.div>
            </form>
            
            {/* Info Cards */}
            <motion.div
              initial={{ opacity: 0, y: 20 }}
              animate={{ opacity: 1, y: 0 }}
              transition={{ delay: 0.4 }}
              className="grid md:grid-cols-3 gap-4 pt-6 border-t border-mystical-mid/20"
            >
              <div className="text-center p-3">
                <Star className="h-6 w-6 text-celestial-mid mx-auto mb-2" />
                <p className="text-sm font-medium text-foreground">Accurate Analysis</p>
                <p className="text-xs text-muted-foreground">Precise calculations</p>
              </div>
              <div className="text-center p-3">
                <Shield className="h-6 w-6 text-celestial-mid mx-auto mb-2" />
                <p className="text-sm font-medium text-foreground">Privacy Protected</p>
                <p className="text-xs text-muted-foreground">Your data is secure</p>
              </div>
              <div className="text-center p-3">
                <Sparkles className="h-6 w-6 text-celestial-mid mx-auto mb-2" />
                <p className="text-sm font-medium text-foreground">Instant Results</p>
                <p className="text-xs text-muted-foreground">Real-time insights</p>
              </div>
            </motion.div>
          </CardContent>
        </Card>
      </motion.div>
    </div>
  );
};

export default BirthProfileForm;
