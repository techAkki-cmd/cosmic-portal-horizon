import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import * as z from 'zod';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Label } from '@/components/ui/label';
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card';
import { Alert, AlertDescription } from '@/components/ui/alert';
import { Separator } from '@/components/ui/separator';
import { CosmicBackground } from '@/components/CosmicBackground';
import { useAuth } from '@/contexts/AuthContext';
import { Star, Eye, EyeOff, Loader2, Calendar, MapPin, Clock, User, Mail } from 'lucide-react';
import { motion } from 'framer-motion';

// ✅ Updated schema to match backend expectations
const signupSchema = z.object({
  username: z.string()
    .min(3, 'Username must be at least 3 characters')
    .max(20, 'Username must not exceed 20 characters')
    .regex(/^[a-zA-Z0-9_]+$/, 'Username can only contain letters, numbers, and underscores'),
  email: z.string().email('Please enter a valid email address'),
  password: z.string()
    .min(6, 'Password must be at least 6 characters')
    .regex(/^(?=.*[A-Za-z])(?=.*\d)/, 'Password must contain at least one letter and one number'),
  confirmPassword: z.string(),
  firstName: z.string().min(1, 'First name is required'),
  lastName: z.string().optional(),
  birthDate: z.string().optional(),
  birthTime: z.string().optional(),
  birthPlace: z.string().optional(),
}).refine((data) => data.password === data.confirmPassword, {
  message: "Passwords don't match",
  path: ["confirmPassword"],
});

type SignupForm = z.infer<typeof signupSchema>;

export default function Signup() {
  const [showPassword, setShowPassword] = useState(false);
  const [showConfirmPassword, setShowConfirmPassword] = useState(false);
  const [error, setError] = useState('');
  const { register: registerUser, isLoading } = useAuth(); // ✅ Destructure register correctly
  const navigate = useNavigate();

  const {
    register,
    handleSubmit,
    formState: { errors },
    setError: setFormError,
  } = useForm<SignupForm>({
    resolver: zodResolver(signupSchema),
  });

  // ✅ Updated onSubmit function to match backend expectations
  // In Signup.tsx - Update the onSubmit function

const onSubmit = async (data: SignupForm) => {
  try {
    setError('');
    
    // ✅ Debug: Log the raw form data
    console.log('📝 Raw form data:', data);
    
    // ✅ Validate required fields on frontend
    if (!data.username || !data.email || !data.password) {
      setError('Please fill in all required fields');
      return;
    }

    if (!data.firstName) {
      setError('First name is required');
      return;
    }

    // ✅ Process birth date/time
    let birthDateTime: string | undefined;
    if (data.birthDate) {
      if (data.birthTime) {
        const combinedDateTime = `${data.birthDate}T${data.birthTime}`;
        birthDateTime = new Date(combinedDateTime).toISOString();
      } else {
        birthDateTime = new Date(`${data.birthDate}T12:00:00Z`).toISOString();
      }
    }

    // ✅ Prepare signup data
    const signupData = {
      username: data.username,
      email: data.email,
      password: data.password,
      firstName: data.firstName,
      lastName: data.lastName || undefined,
      birthDateTime: birthDateTime,
      birthLocation: data.birthPlace || undefined,
      birthLatitude: undefined,
      birthLongitude: undefined,
      timezone: undefined,
    };

    console.log('📤 Sending signup data to AuthContext:', {
      ...signupData,
      password: '***'
    });

    // ✅ Call registerUser with proper error handling
    await registerUser(signupData);
    
    console.log('✅ Registration successful, navigating to dashboard');
    navigate('/dashboard', { replace: true });
    
  } catch (err: any) {
    console.error('❌ Signup form error:', err);
    
    let errorMessage = 'Failed to create account';
    
    if (err?.response?.data) {
      if (err.response.data.message) {
        errorMessage = err.response.data.message;
      } else if (err.response.data.errors) {
        const backendErrors = err.response.data.errors;
        Object.keys(backendErrors).forEach(field => {
          if (['username', 'email', 'password', 'firstName'].includes(field)) {
            setFormError(field as keyof SignupForm, {
              type: 'server',
              message: backendErrors[field]
            });
          }
        });
        errorMessage = 'Please correct the errors above';
      }
    } else if (err?.message) {
      errorMessage = err.message;
    }
    
    setError(errorMessage);
  }
};

  return (
    <div className="min-h-screen bg-background text-foreground flex items-center justify-center p-6">
      <CosmicBackground />
      
      <motion.div
        initial={{ opacity: 0, y: 30 }}
        animate={{ opacity: 1, y: 0 }}
        transition={{ duration: 0.8 }}
        className="relative z-10 w-full max-w-md"
      >
        <Card className="bg-card/80 backdrop-blur-sm border-mystical-mid/20">
          <CardHeader className="text-center">
            <div className="flex justify-center mb-4">
              <div className="p-3 rounded-full bg-gradient-mystical">
                <Star className="h-8 w-8 text-white" />
              </div>
            </div>
            <CardTitle className="text-2xl font-playfair text-foreground">
              Begin Your Journey
            </CardTitle>
            <CardDescription className="text-muted-foreground">
              Create your account to unlock cosmic insights
            </CardDescription>
          </CardHeader>
          
          <CardContent>
            <form onSubmit={handleSubmit(onSubmit)} className="space-y-6">
              {error && (
                <Alert variant="destructive">
                  <AlertDescription>{error}</AlertDescription>
                </Alert>
              )}
              
              {/* Basic Information */}
              <div className="space-y-4">
                {/* ✅ Username Field - NEW */}
                <div className="space-y-2">
                  <Label htmlFor="username" className="text-foreground">
                    Username
                  </Label>
                  <div className="relative">
                    <Input
                      id="username"
                      type="text"
                      placeholder="Choose a unique username"
                      className="bg-input border-border text-foreground pl-10"
                      {...register('username')}
                    />
                    <User className="absolute left-3 top-3 h-4 w-4 text-muted-foreground" />
                  </div>
                  {errors.username && (
                    <p className="text-sm text-destructive">{errors.username.message}</p>
                  )}
                  <p className="text-xs text-muted-foreground">
                    Letters, numbers, and underscores only
                  </p>
                </div>
                
                {/* ✅ Email Field */}
                <div className="space-y-2">
                  <Label htmlFor="email" className="text-foreground">
                    Email
                  </Label>
                  <div className="relative">
                    <Input
                      id="email"
                      type="email"
                      placeholder="Enter your email"
                      className="bg-input border-border text-foreground pl-10"
                      {...register('email')}
                    />
                    <Mail className="absolute left-3 top-3 h-4 w-4 text-muted-foreground" />
                  </div>
                  {errors.email && (
                    <p className="text-sm text-destructive">{errors.email.message}</p>
                  )}
                </div>

                {/* ✅ Name Fields - Split into firstName and lastName */}
                <div className="grid grid-cols-2 gap-4">
                  <div className="space-y-2">
                    <Label htmlFor="firstName" className="text-foreground">
                      First Name
                    </Label>
                    <Input
                      id="firstName"
                      type="text"
                      placeholder="First name"
                      className="bg-input border-border text-foreground"
                      {...register('firstName')}
                    />
                    {errors.firstName && (
                      <p className="text-sm text-destructive">{errors.firstName.message}</p>
                    )}
                  </div>
                  <div className="space-y-2">
                    <Label htmlFor="lastName" className="text-foreground">
                      Last Name
                    </Label>
                    <Input
                      id="lastName"
                      type="text"
                      placeholder="Last name"
                      className="bg-input border-border text-foreground"
                      {...register('lastName')}
                    />
                    {errors.lastName && (
                      <p className="text-sm text-destructive">{errors.lastName.message}</p>
                    )}
                  </div>
                </div>
                
                {/* ✅ Password Field */}
                <div className="space-y-2">
                  <Label htmlFor="password" className="text-foreground">
                    Password
                  </Label>
                  <div className="relative">
                    <Input
                      id="password"
                      type={showPassword ? 'text' : 'password'}
                      placeholder="Create a password"
                      className="bg-input border-border text-foreground pr-10"
                      {...register('password')}
                    />
                    <Button
                      type="button"
                      variant="ghost"
                      size="sm"
                      className="absolute right-0 top-0 h-full px-3 py-2 hover:bg-transparent"
                      onClick={() => setShowPassword(!showPassword)}
                    >
                      {showPassword ? (
                        <EyeOff className="h-4 w-4 text-muted-foreground" />
                      ) : (
                        <Eye className="h-4 w-4 text-muted-foreground" />
                      )}
                    </Button>
                  </div>
                  {errors.password && (
                    <p className="text-sm text-destructive">{errors.password.message}</p>
                  )}
                  <p className="text-xs text-muted-foreground">
                    At least 6 characters with letters and numbers
                  </p>
                </div>
                
                {/* ✅ Confirm Password Field */}
                <div className="space-y-2">
                  <Label htmlFor="confirmPassword" className="text-foreground">
                    Confirm Password
                  </Label>
                  <div className="relative">
                    <Input
                      id="confirmPassword"
                      type={showConfirmPassword ? 'text' : 'password'}
                      placeholder="Confirm your password"
                      className="bg-input border-border text-foreground pr-10"
                      {...register('confirmPassword')}
                    />
                    <Button
                      type="button"
                      variant="ghost"
                      size="sm"
                      className="absolute right-0 top-0 h-full px-3 py-2 hover:bg-transparent"
                      onClick={() => setShowConfirmPassword(!showConfirmPassword)}
                    >
                      {showConfirmPassword ? (
                        <EyeOff className="h-4 w-4 text-muted-foreground" />
                      ) : (
                        <Eye className="h-4 w-4 text-muted-foreground" />
                      )}
                    </Button>
                  </div>
                  {errors.confirmPassword && (
                    <p className="text-sm text-destructive">{errors.confirmPassword.message}</p>
                  )}
                </div>
              </div>

              <Separator className="bg-mystical-mid/20" />

              {/* Birth Information - Optional */}
              <div className="space-y-4">
                <div className="text-center">
                  <h3 className="text-sm font-medium text-foreground mb-1">
                    Birth Information (Optional)
                  </h3>
                  <p className="text-xs text-muted-foreground">
                    For personalized astrology readings
                  </p>
                </div>
                
                <div className="space-y-2">
                  <Label htmlFor="birthDate" className="text-foreground flex items-center gap-2">
                    <Calendar className="h-4 w-4" />
                    Birth Date
                  </Label>
                  <Input
                    id="birthDate"
                    type="date"
                    className="bg-input border-border text-foreground"
                    {...register('birthDate')}
                  />
                  {errors.birthDate && (
                    <p className="text-sm text-destructive">{errors.birthDate.message}</p>
                  )}
                </div>
                
                <div className="space-y-2">
                  <Label htmlFor="birthTime" className="text-foreground flex items-center gap-2">
                    <Clock className="h-4 w-4" />
                    Birth Time
                  </Label>
                  <Input
                    id="birthTime"
                    type="time"
                    className="bg-input border-border text-foreground"
                    {...register('birthTime')}
                  />
                  {errors.birthTime && (
                    <p className="text-sm text-destructive">{errors.birthTime.message}</p>
                  )}
                  <p className="text-xs text-muted-foreground">
                    If unknown, leave blank for noon time
                  </p>
                </div>
                
                <div className="space-y-2">
                  <Label htmlFor="birthPlace" className="text-foreground flex items-center gap-2">
                    <MapPin className="h-4 w-4" />
                    Birth Place
                  </Label>
                  <Input
                    id="birthPlace"
                    type="text"
                    placeholder="City, Country (e.g., New York, USA)"
                    className="bg-input border-border text-foreground"
                    {...register('birthPlace')}
                  />
                  {errors.birthPlace && (
                    <p className="text-sm text-destructive">{errors.birthPlace.message}</p>
                  )}
                </div>
              </div>
              
              <Button
                type="submit"
                className="w-full bg-mystical-mid hover:bg-mystical-bright text-white"
                disabled={isLoading}
              >
                {isLoading ? (
                  <>
                    <Loader2 className="mr-2 h-4 w-4 animate-spin" />
                    Creating Account...
                  </>
                ) : (
                  'Create Account'
                )}
              </Button>
            </form>
            
            <div className="mt-6 text-center">
              <p className="text-muted-foreground">
                Already have an account?{' '}
                <Link 
                  to="/login" 
                  className="text-celestial-mid hover:text-celestial-bright font-medium"
                >
                  Sign in
                </Link>
              </p>
            </div>
            
            <div className="mt-4 text-center">
              <Link 
                to="/" 
                className="text-muted-foreground hover:text-foreground text-sm"
              >
                ← Back to home
              </Link>
            </div>

          </CardContent>
        </Card>
      </motion.div>
    </div>
  );
}
