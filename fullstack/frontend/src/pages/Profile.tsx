// src/pages/Profile.tsx
import React, { useState, useEffect } from 'react';
import { useAuth } from '../contexts/AuthContext';
import { authService } from '../services/authService';
import { Card, CardContent, CardHeader, CardTitle } from '../components/ui/card';
import { Button } from '../components/ui/button';
import { Input } from '../components/ui/input';
import { Label } from '../components/ui/label';
import { Progress } from '../components/ui/progress';
import type { BirthProfileData, BirthTimeSource } from '../types/auth';
import { Separator } from '../components/ui/separator';
import { Alert, AlertDescription } from '../components/ui/alert';
import { Loader2, Save, Edit, MapPin, Calendar, User, Phone, Mail, RefreshCw } from 'lucide-react';
import { toast } from '@/components/ui/use-toast';

export const Profile: React.FC = () => {
  const { user, updateUser, refreshUser } = useAuth();
  
  // Separate editing states for different sections
  const [isEditingBasic, setIsEditingBasic] = useState(false);
  const [isEditingBirth, setIsEditingBirth] = useState(false);
  
  // Loading states
  const [isSavingBasic, setIsSavingBasic] = useState(false);
  const [isSavingBirth, setIsSavingBirth] = useState(false);
  const [isRefreshing, setIsRefreshing] = useState(false);
  
  // Form data states
  const [basicFormData, setBasicFormData] = useState({
    firstName: user?.firstName || '',
    lastName: user?.lastName || '',
    email: user?.email || '',
    phoneNumber: user?.phoneNumber || '',
  });

  const [birthFormData, setBirthFormData] = useState({
    birthDate: '',
    birthTime: '12:00',
    birthLocation: '',
    birthLatitude: null as number | null,
    birthLongitude: null as number | null,
    timezone: Intl.DateTimeFormat().resolvedOptions().timeZone,
  });

  // Initialize form data when user data changes
  useEffect(() => {
    if (user) {
      setBasicFormData({
        firstName: user.firstName || '',
        lastName: user.lastName || '',
        email: user.email || '',
        phoneNumber: user.phoneNumber || '',
      });

      // Parse existing birth data if available
      if (user.birthDateTime) {
        const birthDateTime = new Date(user.birthDateTime);
        setBirthFormData({
          birthDate: birthDateTime.toISOString().split('T')[0],
          birthTime: birthDateTime.toTimeString().slice(0, 5),
          birthLocation: user.birthLocation || '',
          birthLatitude: user.birthLatitude || null,
          birthLongitude: user.birthLongitude || null,
          timezone: user.timezone || Intl.DateTimeFormat().resolvedOptions().timeZone,
        });
      }
    }
  }, [user]);

  // ✅ FIXED: Calculate profile completion percentage
  const calculateCompletionPercentage = (): number => {
    if (!user) return 0;
    
    const fields = [
      user.firstName,
      user.lastName,
      user.email,
      user.phoneNumber,
      user.birthDateTime,
      user.birthLocation,
      user.birthLatitude,
      user.birthLongitude,
    ];
    
    const filledFields = fields.filter(field => 
      field !== null && field !== undefined && field.toString().trim() !== ''
    ).length;
    
    const percentage = Math.round((filledFields / fields.length) * 100);
    
    console.log('🔍 Profile completion calculation:', {
      totalFields: fields.length,
      filledFields: filledFields,
      percentage: percentage,
      fieldStatus: {
        firstName: !!user.firstName,
        lastName: !!user.lastName,
        email: !!user.email,
        phoneNumber: !!user.phoneNumber,
        birthDateTime: !!user.birthDateTime,
        birthLocation: !!user.birthLocation,
        birthLatitude: !!user.birthLatitude,
        birthLongitude: !!user.birthLongitude
      }
    });
    
    return percentage;
  };

  // ✅ FORCE REFRESH: Function for manual data refresh
  const forceUserDataRefresh = async () => {
    setIsRefreshing(true);
    try {
      console.log('🔄 Forcing complete user data refresh...');
      
      // Clear local storage to force fresh fetch
      localStorage.removeItem('cosmic_user_data');
      
      // Refresh from server
      await refreshUser();
      
      console.log('✅ User data force refresh completed');
      
      toast({
        title: "Data Refreshed",
        description: "User profile has been refreshed from server.",
      });
      
    } catch (error) {
      console.error('❌ Force refresh failed:', error);
      
      toast({
        title: "Refresh Failed", 
        description: "Could not refresh user data. Please try logging out and back in.",
        variant: "destructive",
      });
    } finally {
      setIsRefreshing(false);
    }
  };

  // ✅ FIXED: Handle basic information submission
  const handleBasicSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setIsSavingBasic(true);

    try {
      console.log('🔄 Updating basic profile information...');
      
      // Call API to update basic profile
      const updatedProfile = await authService.updateUserProfile({
        firstName: basicFormData.firstName,
        lastName: basicFormData.lastName,
        phoneNumber: basicFormData.phoneNumber,
      });

      // Update context with new data
      if (updatedProfile) {
        updateUser(updatedProfile);
      } else {
        await refreshUser();
      }

      setIsEditingBasic(false);
      
      toast({
        title: "Profile Updated",
        description: "Your basic information has been successfully updated.",
      });

      console.log('✅ Basic profile updated successfully');

    } catch (error) {
      console.error('❌ Failed to update basic profile:', error);
      
      toast({
        title: "Update Failed",
        description: "Failed to update profile. Please try again.",
        variant: "destructive",
      });
    } finally {
      setIsSavingBasic(false);
    }
  };

  // ✅ ENHANCED: Handle birth information submission with robust fallbacks
 // ✅ FIXED: Handle birth information submission with proper typing
const handleBirthSubmit = async (e: React.FormEvent) => {
  e.preventDefault();
  setIsSavingBirth(true);

  try {
    console.log('🔄 Updating birth profile information...');

    // Validate required fields
    if (!birthFormData.birthDate || !birthFormData.birthLocation) {
      throw new Error('Birth date and location are required');
    }

    // Combine date and time
    const birthDateTime = `${birthFormData.birthDate}T${birthFormData.birthTime}:00`;

    // ✅ FIXED: Properly typed birth data payload
    const birthDataPayload: BirthProfileData = {
      birthDateTime,
      birthLocation: birthFormData.birthLocation,
      birthLatitude: birthFormData.birthLatitude,
      birthLongitude: birthFormData.birthLongitude,
      timezone: birthFormData.timezone,
      isTimeAccurate: true,
      timeSource: 'USER_PROVIDED' as const, // ✅ Type assertion for literal type
    };

    console.log('📤 Sending birth data:', birthDataPayload);

    // Call API to update birth profile
    const updatedProfile = await authService.updateBirthProfile(birthDataPayload);

    // ... rest of your existing code
  } catch (error) {
    // ... existing error handling
  } finally {
    setIsSavingBirth(false);
  }
};


  if (!user) {
    return (
      <div className="container mx-auto px-4 py-8">
        <Card>
          <CardContent className="flex items-center justify-center h-48">
            <p>Please log in to view your profile.</p>
          </CardContent>
        </Card>
      </div>
    );
  }

  const completionPercentage = calculateCompletionPercentage();

  return (
    <div className="container mx-auto px-4 py-8 max-w-4xl">
      {/* Profile Header */}
      <div className="mb-8">
        <div className="flex items-center justify-between">
          <div>
            <h1 className="text-3xl font-bold text-gray-900 dark:text-white mb-2">
              Profile Settings
            </h1>
            <p className="text-gray-600 dark:text-gray-400">
              Complete your profile to unlock personalized Vedic insights
            </p>
          </div>
          {/* ✅ NEW: Manual refresh button */}
          <Button
            variant="outline"
            size="sm"
            onClick={forceUserDataRefresh}
            disabled={isRefreshing}
            className="flex items-center gap-2"
          >
            {isRefreshing ? (
              <>
                <Loader2 className="h-4 w-4 animate-spin" />
                Refreshing...
              </>
            ) : (
              <>
                <RefreshCw className="h-4 w-4" />
                Refresh Data
              </>
            )}
          </Button>
        </div>
      </div>

      {/* Profile Completion */}
      <Card className="mb-6">
        <CardHeader>
          <CardTitle className="flex items-center justify-between">
            Profile Completion
            <span className="text-lg font-semibold">
              {completionPercentage}%
            </span>
          </CardTitle>
        </CardHeader>
        <CardContent>
          <Progress value={completionPercentage} className="mb-4" />
          <p className="text-sm text-gray-600 dark:text-gray-400">
            Add more information to improve your astrological readings accuracy
          </p>
        </CardContent>
      </Card>

      {/* ✅ FIXED: Basic Information Form */}
      <Card className="mb-6">
        <CardHeader>
          <CardTitle className="flex items-center justify-between">
            <div className="flex items-center gap-2">
              <User className="h-5 w-5" />
              Basic Information
            </div>
            <Button 
              variant="outline" 
              size="sm"
              onClick={() => setIsEditingBasic(!isEditingBasic)}
              disabled={isSavingBasic}
            >
              {isEditingBasic ? 'Cancel' : <><Edit className="h-4 w-4 mr-1" /> Edit</>}
            </Button>
          </CardTitle>
        </CardHeader>
        <CardContent>
          <form onSubmit={handleBasicSubmit} className="space-y-4">
            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
              <div>
                <Label htmlFor="firstName">First Name</Label>
                <Input
                  id="firstName"
                  value={basicFormData.firstName}
                  onChange={(e) => setBasicFormData(prev => ({ ...prev, firstName: e.target.value }))}
                  disabled={!isEditingBasic || isSavingBasic}
                  placeholder="Enter your first name"
                />
              </div>
              <div>
                <Label htmlFor="lastName">Last Name</Label>
                <Input
                  id="lastName"
                  value={basicFormData.lastName}
                  onChange={(e) => setBasicFormData(prev => ({ ...prev, lastName: e.target.value }))}
                  disabled={!isEditingBasic || isSavingBasic}
                  placeholder="Enter your last name"
                />
              </div>
              <div>
                <Label htmlFor="email">Email</Label>
                <div className="relative">
                  <Mail className="absolute left-3 top-3 h-4 w-4 text-gray-400" />
                  <Input
                    id="email"
                    type="email"
                    value={basicFormData.email}
                    disabled={true}
                    placeholder="Your email address"
                    className="pl-10"
                  />
                </div>
              </div>
              <div>
                <Label htmlFor="phoneNumber">Phone Number</Label>
                <div className="relative">
                  <Phone className="absolute left-3 top-3 h-4 w-4 text-gray-400" />
                  <Input
                    id="phoneNumber"
                    value={basicFormData.phoneNumber}
                    onChange={(e) => setBasicFormData(prev => ({ ...prev, phoneNumber: e.target.value }))}
                    disabled={!isEditingBasic || isSavingBasic}
                    placeholder="Enter your phone number"
                    className="pl-10"
                  />
                </div>
              </div>
            </div>

            {isEditingBasic && (
              <div className="flex gap-2 pt-4">
                <Button 
                  type="submit" 
                  disabled={isSavingBasic}
                  className="flex items-center gap-2"
                >
                  {isSavingBasic ? (
                    <>
                      <Loader2 className="h-4 w-4 animate-spin" />
                      Saving...
                    </>
                  ) : (
                    <>
                      <Save className="h-4 w-4" />
                      Save Changes
                    </>
                  )}
                </Button>
                <Button 
                  type="button" 
                  variant="outline"
                  onClick={() => setIsEditingBasic(false)}
                  disabled={isSavingBasic}
                >
                  Cancel
                </Button>
              </div>
            )}
          </form>
        </CardContent>
      </Card>

      {/* ✅ FIXED: Birth Information Form */}
      <Card className="mb-6">
        <CardHeader>
          <CardTitle className="flex items-center justify-between">
            <div className="flex items-center gap-2">
              <Calendar className="h-5 w-5" />
              Birth Information
            </div>
            <Button 
              variant="outline" 
              size="sm"
              onClick={() => setIsEditingBirth(!isEditingBirth)}
              disabled={isSavingBirth}
            >
              {isEditingBirth ? 'Cancel' : <><Edit className="h-4 w-4 mr-1" /> Edit</>}
            </Button>
          </CardTitle>
          <p className="text-sm text-gray-600 dark:text-gray-400">
            Required for accurate Vedic astrological calculations
          </p>
        </CardHeader>
        <CardContent>
          <form onSubmit={handleBirthSubmit} className="space-y-4">
            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
              <div>
                <Label htmlFor="birthDate">Birth Date *</Label>
                <Input
                  id="birthDate"
                  type="date"
                  value={birthFormData.birthDate}
                  onChange={(e) => setBirthFormData(prev => ({ ...prev, birthDate: e.target.value }))}
                  disabled={!isEditingBirth || isSavingBirth}
                  required
                />
              </div>
              <div>
                <Label htmlFor="birthTime">Birth Time</Label>
                <Input
                  id="birthTime"
                  type="time"
                  value={birthFormData.birthTime}
                  onChange={(e) => setBirthFormData(prev => ({ ...prev, birthTime: e.target.value }))}
                  disabled={!isEditingBirth || isSavingBirth}
                />
              </div>
              <div className="md:col-span-2">
                <Label htmlFor="birthLocation">Birth Location *</Label>
                <div className="relative">
                  <MapPin className="absolute left-3 top-3 h-4 w-4 text-gray-400" />
                  <Input
                    id="birthLocation"
                    value={birthFormData.birthLocation}
                    onChange={(e) => setBirthFormData(prev => ({ ...prev, birthLocation: e.target.value }))}
                    disabled={!isEditingBirth || isSavingBirth}
                    placeholder="City, State, Country"
                    className="pl-10"
                    required
                  />
                </div>
              </div>
            </div>

            {/* Coordinates (optional) */}
            <div className="grid grid-cols-2 gap-4">
              <div>
                <Label htmlFor="birthLatitude">Latitude (optional)</Label>
                <Input
                  id="birthLatitude"
                  type="number"
                  step="any"
                  placeholder="e.g. 40.7128"
                  value={birthFormData.birthLatitude || ''}
                  onChange={(e) => setBirthFormData(prev => ({ 
                    ...prev, 
                    birthLatitude: e.target.value ? parseFloat(e.target.value) : null 
                  }))}
                  disabled={!isEditingBirth || isSavingBirth}
                />
              </div>
              <div>
                <Label htmlFor="birthLongitude">Longitude (optional)</Label>
                <Input
                  id="birthLongitude"
                  type="number"
                  step="any" 
                  placeholder="e.g. -74.0060"
                  value={birthFormData.birthLongitude || ''}
                  onChange={(e) => setBirthFormData(prev => ({ 
                    ...prev, 
                    birthLongitude: e.target.value ? parseFloat(e.target.value) : null 
                  }))}
                  disabled={!isEditingBirth || isSavingBirth}
                />
              </div>
            </div>
            
            {isEditingBirth && (
              <div className="flex gap-2 pt-4">
                <Button 
                  type="submit" 
                  disabled={isSavingBirth}
                  className="flex items-center gap-2"
                >
                  {isSavingBirth ? (
                    <>
                      <Loader2 className="h-4 w-4 animate-spin" />
                      Saving Birth Data...
                    </>
                  ) : (
                    <>
                      <Save className="h-4 w-4" />
                      Save Birth Information
                    </>
                  )}
                </Button>
                <Button 
                  type="button" 
                  variant="outline"
                  onClick={() => setIsEditingBirth(false)}
                  disabled={isSavingBirth}
                >
                  Cancel
                </Button>
              </div>
            )}

            {/* Birth data completion status */}
            {!user.hasCompleteBirthData && !isEditingBirth && (
              <Alert className="bg-yellow-50 dark:bg-yellow-900/20 border-yellow-200 dark:border-yellow-800">
                <Calendar className="h-4 w-4 text-yellow-600" />
                <AlertDescription className="text-yellow-800 dark:text-yellow-200">
                  ⭐ Complete your birth information to unlock personalized Vedic readings and predictions
                </AlertDescription>
              </Alert>
            )}
          </form>
        </CardContent>
      </Card>

      {/* Account Information */}
      <Card>
        <CardHeader>
          <CardTitle>Account Information</CardTitle>
        </CardHeader>
        <CardContent>
          <div className="grid grid-cols-1 md:grid-cols-2 gap-4 text-sm">
            <div>
              <Label className="text-gray-600 dark:text-gray-400">Username</Label>
              <p className="font-medium">{user.username}</p>
            </div>
            <div>
              <Label className="text-gray-600 dark:text-gray-400">Role</Label>
              <p className="font-medium">{user.role}</p>
            </div>
            <div>
              <Label className="text-gray-600 dark:text-gray-400">Email Verified</Label>
              <p className="font-medium">
                {user.emailVerified ? '✅ Verified' : '⚠️ Not Verified'}
              </p>
            </div>
            <div>
              <Label className="text-gray-600 dark:text-gray-400">Member Since</Label>
              <p className="font-medium">
                {user.createdAt ? new Date(user.createdAt).toLocaleDateString() : 'Unknown'}
              </p>
            </div>
          </div>
        </CardContent>
      </Card>
    </div>
  );
};

export default Profile;
