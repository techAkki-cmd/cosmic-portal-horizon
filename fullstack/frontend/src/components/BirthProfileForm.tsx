import React, { useState } from 'react';
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Label } from '@/components/ui/label';
import { authService } from '@/services/authService';
import { Calendar, MapPin, Clock, Star } from 'lucide-react';

interface BirthProfileFormProps {
  user: any;
  onComplete: () => void;
}

export const BirthProfileForm: React.FC<BirthProfileFormProps> = ({ user, onComplete }) => {
  const [formData, setFormData] = useState({
    birthDate: '',
    birthTime: '12:00',
    birthLocation: '',
    birthLatitude: null as number | null,
    birthLongitude: null as number | null,
    timezone: 'UTC'
  });
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [error, setError] = useState('');

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setIsSubmitting(true);
    setError('');

    try {
      // Validate required fields
      if (!formData.birthDate || !formData.birthLocation) {
        throw new Error('Birth date and location are required');
      }

      // Combine date and time
      const birthDateTime = `${formData.birthDate}T${formData.birthTime}:00`;
      
      // Update user profile with birth data
      await authService.updateBirthProfile({
        birthDateTime,
        birthLocation: formData.birthLocation,
        birthLatitude: formData.birthLatitude,
        birthLongitude: formData.birthLongitude,
        timezone: formData.timezone
      });

      onComplete();
    } catch (error) {
      console.error('Failed to update birth profile:', error);
      const errorMessage = error instanceof Error ? error.message : 'Failed to update birth profile. Please try again.';
      setError(errorMessage);
    } finally {
      setIsSubmitting(false);
    }
  };

  // Helper function to get coordinates from location name (optional enhancement)
  const handleLocationChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const location = e.target.value;
    setFormData({ ...formData, birthLocation: location });
    
    // Optional: You could add geocoding here to automatically fill lat/lng
    // For now, we'll let the backend handle location to coordinates conversion
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-background p-6">
      <Card className="w-full max-w-md bg-card/80 backdrop-blur-sm">
        <CardHeader className="text-center">
          <div className="flex justify-center mb-4">
            <div className="p-3 rounded-full bg-gradient-to-r from-purple-500 to-pink-500">
              <Star className="h-8 w-8 text-white" />
            </div>
          </div>
          <CardTitle className="text-2xl font-playfair">Complete Your Cosmic Profile</CardTitle>
          <CardDescription>
            Add your birth information to unlock personalized astrological insights
          </CardDescription>
        </CardHeader>
        
        <CardContent>
          <form onSubmit={handleSubmit} className="space-y-4">
            {error && (
              <div className="p-3 rounded-lg bg-red-50 border border-red-200 text-red-700 text-sm">
                {error}
              </div>
            )}
            
            <div className="space-y-2">
              <Label htmlFor="birthDate" className="flex items-center gap-2">
                <Calendar className="h-4 w-4" />
                Birth Date *
              </Label>
              <Input
                id="birthDate"
                type="date"
                value={formData.birthDate}
                onChange={(e) => setFormData({ ...formData, birthDate: e.target.value })}
                required
                className="bg-input border-border"
              />
            </div>
            
            <div className="space-y-2">
              <Label htmlFor="birthTime" className="flex items-center gap-2">
                <Clock className="h-4 w-4" />
                Birth Time
              </Label>
              <Input
                id="birthTime"
                type="time"
                value={formData.birthTime}
                onChange={(e) => setFormData({ ...formData, birthTime: e.target.value })}
                className="bg-input border-border"
              />
              <p className="text-xs text-muted-foreground">
                If unknown, leave as noon for best approximation
              </p>
            </div>
            
            <div className="space-y-2">
              <Label htmlFor="birthLocation" className="flex items-center gap-2">
                <MapPin className="h-4 w-4" />
                Birth Location *
              </Label>
              <Input
                id="birthLocation"
                type="text"
                placeholder="City, Country"
                value={formData.birthLocation}
                onChange={handleLocationChange}
                required
                className="bg-input border-border"
              />
              <p className="text-xs text-muted-foreground">
                e.g., New York, USA or London, UK
              </p>
            </div>

            {/* Optional: Manual coordinates input */}
            <div className="grid grid-cols-2 gap-4">
              <div className="space-y-2">
                <Label htmlFor="birthLatitude" className="text-sm">
                  Latitude (optional)
                </Label>
                <Input
                  id="birthLatitude"
                  type="number"
                  step="any"
                  placeholder="e.g. 40.7128"
                  value={formData.birthLatitude || ''}
                  onChange={(e) => setFormData({ 
                    ...formData, 
                    birthLatitude: e.target.value ? parseFloat(e.target.value) : null 
                  })}
                  className="bg-input border-border"
                />
              </div>
              <div className="space-y-2">
                <Label htmlFor="birthLongitude" className="text-sm">
                  Longitude (optional)
                </Label>
                <Input
                  id="birthLongitude"
                  type="number"
                  step="any"
                  placeholder="e.g. -74.0060"
                  value={formData.birthLongitude || ''}
                  onChange={(e) => setFormData({ 
                    ...formData, 
                    birthLongitude: e.target.value ? parseFloat(e.target.value) : null 
                  })}
                  className="bg-input border-border"
                />
              </div>
            </div>

            <div className="space-y-2">
              <Label htmlFor="timezone" className="text-sm">
                Timezone
              </Label>
              <select
                id="timezone"
                value={formData.timezone}
                onChange={(e) => setFormData({ ...formData, timezone: e.target.value })}
                className="w-full px-3 py-2 border border-border rounded-md bg-input"
              >
                <option value="UTC">UTC</option>
                <option value="America/New_York">Eastern Time (US)</option>
                <option value="America/Chicago">Central Time (US)</option>
                <option value="America/Denver">Mountain Time (US)</option>
                <option value="America/Los_Angeles">Pacific Time (US)</option>
                <option value="Europe/London">London</option>
                <option value="Europe/Paris">Paris</option>
                <option value="Asia/Tokyo">Tokyo</option>
                <option value="Asia/Kolkata">India Standard Time</option>
                <option value="Australia/Sydney">Sydney</option>
              </select>
            </div>
            
            <Button
              type="submit"
              className="w-full bg-gradient-to-r from-purple-500 to-pink-500 hover:from-purple-600 hover:to-pink-600 text-white"
              disabled={isSubmitting}
            >
              {isSubmitting ? 'Calculating...' : 'Unlock Personalized Insights'}
            </Button>
          </form>
        </CardContent>
      </Card>
    </div>
  );
};
