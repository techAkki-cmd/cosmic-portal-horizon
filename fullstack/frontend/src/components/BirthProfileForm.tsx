import React, { useState } from 'react';
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Label } from '@/components/ui/label';
import { Alert, AlertDescription } from '@/components/ui/alert';
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from '@/components/ui/select';
import { useAuth } from '@/contexts/AuthContext'; // ✅ Use our AuthContext
import { authService } from '@/services/authService';
import { Calendar, MapPin, Clock, Star, Loader2, AlertCircle, CheckCircle } from 'lucide-react';

interface BirthProfileFormProps {
  onComplete?: () => void;
  onCancel?: () => void;
}

export const BirthProfileForm: React.FC<BirthProfileFormProps> = ({ 
  onComplete, 
  onCancel 
}) => {
  const { user, updateUser, refreshUser } = useAuth(); // ✅ Use AuthContext
  
  const [formData, setFormData] = useState({
    birthDate: user?.birthDateTime ? user.birthDateTime.split('T')[0] : '',
    birthTime: user?.birthDateTime ? user.birthDateTime.split('T')[1]?.substring(0, 5) || '12:00' : '12:00',
    birthLocation: user?.birthLocation || '',
    birthLatitude: user?.birthLatitude || null,
    birthLongitude: user?.birthLongitude || null,
    timezone: user?.timezone || Intl.DateTimeFormat().resolvedOptions().timeZone
  });
  
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState(false);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setIsSubmitting(true);
    setError('');
    setSuccess(false);

    try {
      // Validate required fields
      if (!formData.birthDate || !formData.birthLocation) {
        throw new Error('Birth date and location are required');
      }

      // Combine date and time
      const birthDateTime = `${formData.birthDate}T${formData.birthTime}:00`;
      
      // Update user profile with birth data
      const updatedProfile = await authService.updateBirthProfile({
        birthDateTime,
        birthLocation: formData.birthLocation,
        birthLatitude: formData.birthLatitude,
        birthLongitude: formData.birthLongitude,
        timezone: formData.timezone
      });

      // ✅ Update AuthContext with new user data
      if (updatedProfile) {
        updateUser(updatedProfile);
      } else {
        await refreshUser(); // Refresh user data from server
      }

      setSuccess(true);
      
      // Call completion callback after a brief delay
      setTimeout(() => {
        onComplete?.();
      }, 1500);

    } catch (error) {
      console.error('Failed to update birth profile:', error);
      let errorMessage = 'Failed to update birth profile. Please try again.';
      
      if (error instanceof Error) {
        errorMessage = error.message;
      } else if (error?.response?.data?.message) {
        errorMessage = error.response.data.message;
      }
      
      setError(errorMessage);
    } finally {
      setIsSubmitting(false);
    }
  };

  // Enhanced timezone options
  const timezones = [
    { value: 'UTC', label: 'UTC (Coordinated Universal Time)' },
    { value: 'America/New_York', label: 'Eastern Time (US & Canada)' },
    { value: 'America/Chicago', label: 'Central Time (US & Canada)' },
    { value: 'America/Denver', label: 'Mountain Time (US & Canada)' },
    { value: 'America/Los_Angeles', label: 'Pacific Time (US & Canada)' },
    { value: 'Europe/London', label: 'London (GMT/BST)' },
    { value: 'Europe/Paris', label: 'Paris (CET/CEST)' },
    { value: 'Asia/Kolkata', label: 'India Standard Time' },
    { value: 'Asia/Tokyo', label: 'Japan Standard Time' },
    { value: 'Australia/Sydney', label: 'Sydney (AEST/AEDT)' }
  ];

  if (success) {
    return (
      <div className="min-h-screen flex items-center justify-center bg-background p-6">
        <Card className="w-full max-w-md bg-card/80 backdrop-blur-sm">
          <CardContent className="pt-6">
            <div className="text-center space-y-4">
              <div className="flex justify-center">
                <div className="p-3 rounded-full bg-gradient-to-r from-green-500 to-emerald-500">
                  <CheckCircle className="h-8 w-8 text-white" />
                </div>
              </div>
              <h3 className="text-xl font-semibold text-foreground">Profile Updated!</h3>
              <p className="text-muted-foreground">
                Your cosmic profile is now complete. Personalized insights await!
              </p>
            </div>
          </CardContent>
        </Card>
      </div>
    );
  }

  return (
    <div className="min-h-screen flex items-center justify-center bg-background p-6">
      <Card className="w-full max-w-lg bg-card/80 backdrop-blur-sm border-mystical-mid/20">
        <CardHeader className="text-center">
          <div className="flex justify-center mb-4">
            <div className="p-3 rounded-full bg-gradient-to-r from-purple-500 to-pink-500">
              <Star className="h-8 w-8 text-white" />
            </div>
          </div>
          <CardTitle className="text-2xl font-playfair">Complete Your Cosmic Profile</CardTitle>
          <CardDescription>
            Add your birth information to unlock personalized Vedic astrological insights
          </CardDescription>
        </CardHeader>
        
        <CardContent>
          <form onSubmit={handleSubmit} className="space-y-6">
            {error && (
              <Alert variant="destructive">
                <AlertCircle className="h-4 w-4" />
                <AlertDescription>{error}</AlertDescription>
              </Alert>
            )}
            
            <div className="space-y-2">
              <Label htmlFor="birthDate" className="flex items-center gap-2 text-foreground">
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
              <Label htmlFor="birthTime" className="flex items-center gap-2 text-foreground">
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
              <Label htmlFor="birthLocation" className="flex items-center gap-2 text-foreground">
                <MapPin className="h-4 w-4" />
                Birth Location *
              </Label>
              <Input
                id="birthLocation"
                type="text"
                placeholder="City, State/Province, Country"
                value={formData.birthLocation}
                onChange={(e) => setFormData({ ...formData, birthLocation: e.target.value })}
                required
                className="bg-input border-border"
              />
              <p className="text-xs text-muted-foreground">
                Be specific for accurate coordinates (e.g., "Mumbai, Maharashtra, India")
              </p>
            </div>

            {/* Enhanced Coordinates Section */}
            <div className="grid grid-cols-2 gap-4">
              <div className="space-y-2">
                <Label htmlFor="birthLatitude" className="text-sm text-foreground">
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
                <Label htmlFor="birthLongitude" className="text-sm text-foreground">
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
              <Label className="text-sm text-foreground">Timezone</Label>
              <Select 
                value={formData.timezone} 
                onValueChange={(value) => setFormData({ ...formData, timezone: value })}
              >
                <SelectTrigger className="bg-input border-border">
                  <SelectValue placeholder="Select timezone" />
                </SelectTrigger>
                <SelectContent>
                  {timezones.map((tz) => (
                    <SelectItem key={tz.value} value={tz.value}>
                      {tz.label}
                    </SelectItem>
                  ))}
                </SelectContent>
              </Select>
            </div>
            
            <div className="flex gap-3 pt-4">
              {onCancel && (
                <Button
                  type="button"
                  variant="outline"
                  onClick={onCancel}
                  className="flex-1"
                  disabled={isSubmitting}
                >
                  Cancel
                </Button>
              )}
              <Button
                type="submit"
                className="flex-1 bg-gradient-to-r from-purple-500 to-pink-500 hover:from-purple-600 hover:to-pink-600 text-white"
                disabled={isSubmitting}
              >
                {isSubmitting ? (
                  <>
                    <Loader2 className="mr-2 h-4 w-4 animate-spin" />
                    Updating Profile...
                  </>
                ) : (
                  'Complete Profile'
                )}
              </Button>
            </div>
          </form>
        </CardContent>
      </Card>
    </div>
  );
};
