import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useLocation } from 'react-router-dom';
import { useForm, Controller } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import * as z from 'zod';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Label } from '@/components/ui/label';
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card';
import { Alert, AlertDescription } from '@/components/ui/alert';
import { Checkbox } from '@/components/ui/checkbox';
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from '@/components/ui/select';
import { Separator } from '@/components/ui/separator';
import { CosmicBackground } from '@/components/CosmicBackground';
import { useAuth } from '@/contexts/AuthContext';
import { 
  Star, Eye, EyeOff, Loader2, User, Mail, Shield, Globe, 
  Smartphone, Monitor, Tablet, AlertCircle, CheckCircle, 
  Info, Lock, Clock, MapPin
} from 'lucide-react';
import { motion, AnimatePresence } from 'framer-motion';

// ================ COMPREHENSIVE VALIDATION SCHEMA ================

const loginSchema = z.object({
  // Main credentials - supports both username and email
  username: z.string()
    .min(3, 'Username or email must be at least 3 characters')
    .max(100, 'Username or email cannot exceed 100 characters'),
  
  password: z.string()
    .min(6, 'Password must be at least 6 characters')
    .max(100, 'Password cannot exceed 100 characters'),
  
  // Security and preferences
  rememberMe: z.boolean().default(false),
  twoFactorCode: z.string()
    .regex(/^\d{6}$/, '2FA code must be 6 digits')
    .optional(),
  
  // Localization
  language: z.enum(['en', 'es', 'fr', 'de', 'hi', 'sa']).default('en'),
  timezone: z.string().optional(),
  
  // Device information (auto-populated)
  deviceType: z.enum(['DESKTOP', 'MOBILE', 'TABLET']).optional(),
});

type LoginForm = z.infer<typeof loginSchema>;

// ================ INTERFACES ================

interface LoginState {
  showPassword: boolean;
  show2FA: boolean;
  needsTwoFactor: boolean;
  isEmailLogin: boolean;
  clientInfo: {
    ip: string;
    userAgent: string;
    deviceType: string;
    timezone: string;
  };
}

// ================ CONSTANTS ================

const LANGUAGES = [
  { value: 'en', label: 'English', flag: '🇺🇸' },
  { value: 'es', label: 'Español', flag: '🇪🇸' },
  { value: 'fr', label: 'Français', flag: '🇫🇷' },
  { value: 'de', label: 'Deutsch', flag: '🇩🇪' },
  { value: 'hi', label: 'हिन्दी', flag: '🇮🇳' },
  { value: 'sa', label: 'संस्कृतम्', flag: '🕉️' }
];

const RECENT_LOGINS_KEY = 'cosmic_recent_logins';
const MAX_RECENT_LOGINS = 3;

// ================ MAIN COMPONENT ================

export default function Login() {
  // State Management
  const [loginState, setLoginState] = useState<LoginState>({
    showPassword: false,
    show2FA: false,
    needsTwoFactor: false,
    isEmailLogin: false,
    clientInfo: {
      ip: '',
      userAgent: navigator.userAgent,
      deviceType: getDeviceType(),
      timezone: Intl.DateTimeFormat().resolvedOptions().timeZone
    }
  });

  const [error, setError] = useState('');
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [recentLogins, setRecentLogins] = useState<string[]>([]);
  const [showAdvancedOptions, setShowAdvancedOptions] = useState(false);

  const { login, isLoading } = useAuth();
  const navigate = useNavigate();
  const location = useLocation();

  const from = location.state?.from?.pathname || '/dashboard';

  // Form Setup
  const {
    register,
    handleSubmit,
    control,
    watch,
    setValue,
    formState: { errors, isValid },
    setError: setFormError,
    clearErrors
  } = useForm<LoginForm>({
    resolver: zodResolver(loginSchema),
    mode: 'onChange',
    defaultValues: {
      language: 'en',
      rememberMe: false,
      timezone: loginState.clientInfo.timezone,
      deviceType: loginState.clientInfo.deviceType as any
    }
  });

  // Watch username to detect email vs username
  const watchedUsername = watch('username');
  const watchedLanguage = watch('language');

  // ================ EFFECTS ================

  useEffect(() => {
    initializeComponent();
  }, []);

  useEffect(() => {
    // Update login type detection
    setLoginState(prev => ({
      ...prev,
      isEmailLogin: isEmailAddress(watchedUsername)
    }));
  }, [watchedUsername]);

  useEffect(() => {
    // Load recent logins from localStorage
    loadRecentLogins();
  }, []);

  useEffect(() => {
    // Get client IP
    getClientIP();
  }, []);

  // ================ INITIALIZATION ================

  const initializeComponent = async () => {
    try {
      // Set timezone and device info
      setValue('timezone', loginState.clientInfo.timezone);
      setValue('deviceType', loginState.clientInfo.deviceType as any);

      // Check for redirect messages
      const urlParams = new URLSearchParams(location.search);
      const message = urlParams.get('message');
      if (message === 'session_expired') {
        setError('Your session has expired. Please sign in again.');
      } else if (message === 'account_created') {
        // Show success message for new accounts
        console.log('✅ Account created successfully');
      }

      // Auto-focus username field
      setTimeout(() => {
        const usernameInput = document.getElementById('username');
        if (usernameInput) {
          usernameInput.focus();
        }
      }, 100);

    } catch (error) {
      console.error('Initialization error:', error);
    }
  };

  // ================ UTILITY FUNCTIONS ================

  function getDeviceType(): string {
    const userAgent = navigator.userAgent;
    if (/Mobi|Android/i.test(userAgent)) return 'MOBILE';
    if (/Tablet|iPad/i.test(userAgent)) return 'TABLET';
    return 'DESKTOP';
  }

  const getClientIP = async (): Promise<void> => {
    try {
      const response = await fetch('https://api.ipify.org?format=json');
      const data = await response.json();
      setLoginState(prev => ({
        ...prev,
        clientInfo: { ...prev.clientInfo, ip: data.ip }
      }));
    } catch (error) {
      console.warn('Could not fetch client IP:', error);
      setLoginState(prev => ({
        ...prev,
        clientInfo: { ...prev.clientInfo, ip: '127.0.0.1' }
      }));
    }
  };

  const isEmailAddress = (identifier: string): boolean => {
    return identifier && identifier.includes('@');
  };

  const loadRecentLogins = (): void => {
    try {
      const stored = localStorage.getItem(RECENT_LOGINS_KEY);
      if (stored) {
        const parsed = JSON.parse(stored);
        setRecentLogins(Array.isArray(parsed) ? parsed : []);
      }
    } catch (error) {
      console.warn('Could not load recent logins:', error);
    }
  };

  const saveRecentLogin = (username: string): void => {
    try {
      const updated = [username, ...recentLogins.filter(u => u !== username)]
        .slice(0, MAX_RECENT_LOGINS);
      setRecentLogins(updated);
      localStorage.setItem(RECENT_LOGINS_KEY, JSON.stringify(updated));
    } catch (error) {
      console.warn('Could not save recent login:', error);
    }
  };

  // ================ FORM SUBMISSION ================

  const onSubmit = async (data: LoginForm) => {
    try {
      setError('');
      setIsSubmitting(true);
      clearErrors();

      console.log('🚀 Attempting login with comprehensive data:', {
        username: data.username,
        password: '***',
        rememberMe: data.rememberMe,
        language: data.language,
        deviceType: data.deviceType,
        timezone: data.timezone,
        hasTwoFactor: !!data.twoFactorCode
      });

      // Prepare comprehensive login data matching backend DTO
      const loginData = {
        username: data.username.trim(),
        password: data.password,
        rememberMe: data.rememberMe,
        clientIp: loginState.clientInfo.ip,
        userAgent: loginState.clientInfo.userAgent,
        language: data.language,
        deviceType: data.deviceType,
        twoFactorCode: data.twoFactorCode || undefined,
        timezone: data.timezone
      };

      // Call login with comprehensive data
      await login(loginData);

      // Save to recent logins
      saveRecentLogin(data.username);

      console.log('✅ Login successful, navigating to:', from);
      navigate(from, { replace: true });

    } catch (err: any) {
      console.error('❌ Login error:', err);
      handleLoginError(err);
    } finally {
      setIsSubmitting(false);
    }
  };

  // ================ ERROR HANDLING ================

  const handleLoginError = (err: any) => {
    let errorMessage = 'Login failed. Please try again.';
    
    if (err?.response?.data) {
      const responseData = err.response.data;
      
      // Handle specific error types
      if (responseData.code === 'TWO_FACTOR_REQUIRED') {
        setLoginState(prev => ({ ...prev, needsTwoFactor: true, show2FA: true }));
        errorMessage = 'Please enter your 2FA code to continue.';
      } else if (responseData.code === 'ACCOUNT_LOCKED') {
        errorMessage = 'Your account has been temporarily locked due to multiple failed login attempts. Please try again later or contact support.';
      } else if (responseData.code === 'EMAIL_NOT_VERIFIED') {
        errorMessage = 'Please verify your email address before signing in. Check your inbox for a verification link.';
      } else if (responseData.message) {
        errorMessage = responseData.message;
      } else if (responseData.errors) {
        // Handle field-specific validation errors
        Object.keys(responseData.errors).forEach(field => {
          if (field === 'username' || field === 'password' || field === 'twoFactorCode') {
            setFormError(field as keyof LoginForm, {
              type: 'server',
              message: responseData.errors[field]
            });
          }
        });
        errorMessage = 'Please correct the errors above';
      }
    } else if (err?.message) {
      errorMessage = err.message;
    }
    
    setError(errorMessage);

    // Auto-focus relevant field based on error
    setTimeout(() => {
      if (errorMessage.includes('2FA') || errorMessage.includes('two-factor')) {
        const twoFactorInput = document.getElementById('twoFactorCode');
        if (twoFactorInput) twoFactorInput.focus();
      } else if (errors.password) {
        const passwordInput = document.getElementById('password');
        if (passwordInput) passwordInput.focus();
      }
    }, 100);
  };

  // ================ EVENT HANDLERS ================

  const handleRecentLoginClick = (username: string) => {
    setValue('username', username);
    const passwordInput = document.getElementById('password');
    if (passwordInput) {
      passwordInput.focus();
    }
  };

  const handleForgotPassword = () => {
    const username = watch('username');
    if (username) {
      navigate('/forgot-password', { state: { email: username } });
    } else {
      navigate('/forgot-password');
    }
  };

  // ================ RENDER METHODS ================

  const renderLoginTypeIndicator = () => {
    if (!watchedUsername) return null;

    return (
      <motion.div
        initial={{ opacity: 0, scale: 0.9 }}
        animate={{ opacity: 1, scale: 1 }}
        className="flex items-center gap-2 text-xs text-muted-foreground mb-2"
      >
        {loginState.isEmailLogin ? (
          <>
            <Mail className="h-3 w-3" />
            <span>Signing in with email</span>
          </>
        ) : (
          <>
            <User className="h-3 w-3" />
            <span>Signing in with username</span>
          </>
        )}
      </motion.div>
    );
  };

  const renderRecentLogins = () => {
    if (recentLogins.length === 0) return null;

    return (
      <motion.div
        initial={{ opacity: 0, y: 10 }}
        animate={{ opacity: 1, y: 0 }}
        className="mb-4"
      >
        <Label className="text-sm text-muted-foreground mb-2 block">Recent Logins</Label>
        <div className="flex flex-wrap gap-2">
          {recentLogins.map((username, index) => (
            <Button
              key={index}
              type="button"
              variant="outline"
              size="sm"
              onClick={() => handleRecentLoginClick(username)}
              className="text-xs h-8 px-3 border-mystical-mid/20 hover:border-mystical-mid hover:bg-mystical-mid/10"
            >
              <User className="h-3 w-3 mr-1" />
              {username}
            </Button>
          ))}
        </div>
      </motion.div>
    );
  };

  const renderTwoFactorField = () => {
    if (!loginState.show2FA && !loginState.needsTwoFactor) return null;

    return (
      <motion.div
        initial={{ opacity: 0, height: 0 }}
        animate={{ opacity: 1, height: 'auto' }}
        exit={{ opacity: 0, height: 0 }}
        className="space-y-2"
      >
        <Label htmlFor="twoFactorCode" className="text-foreground flex items-center gap-2">
          <Shield className="h-4 w-4" />
          Two-Factor Authentication Code
        </Label>
        <Input
          id="twoFactorCode"
          type="text"
          placeholder="Enter 6-digit code"
          maxLength={6}
          className="bg-input border-border text-foreground text-center text-lg tracking-widest"
          {...register('twoFactorCode')}
        />
        {errors.twoFactorCode && (
          <p className="text-sm text-destructive flex items-center gap-1">
            <AlertCircle className="h-3 w-3" />
            {errors.twoFactorCode.message}
          </p>
        )}
        <p className="text-xs text-muted-foreground flex items-center gap-1">
          <Info className="h-3 w-3" />
          Check your authenticator app for the current code
        </p>
      </motion.div>
    );
  };

  const renderAdvancedOptions = () => {
    if (!showAdvancedOptions) return null;

    return (
      <motion.div
        initial={{ opacity: 0, height: 0 }}
        animate={{ opacity: 1, height: 'auto' }}
        exit={{ opacity: 0, height: 0 }}
        className="space-y-4 pt-4 border-t border-border"
      >
        {/* Language Selection */}
        <div className="space-y-2">
          <Label className="text-foreground flex items-center gap-2">
            <Globe className="h-4 w-4" />
            Language
          </Label>
          <Controller
            name="language"
            control={control}
            render={({ field }) => (
              <Select onValueChange={field.onChange} value={field.value}>
                <SelectTrigger className="bg-input border-border text-foreground">
                  <SelectValue placeholder="Select language" />
                </SelectTrigger>
                <SelectContent>
                  {LANGUAGES.map(lang => (
                    <SelectItem key={lang.value} value={lang.value}>
                      <div className="flex items-center gap-2">
                        <span>{lang.flag}</span>
                        <span>{lang.label}</span>
                      </div>
                    </SelectItem>
                  ))}
                </SelectContent>
              </Select>
            )}
          />
        </div>

        {/* Device Information */}
        <div className="bg-muted/50 p-3 rounded-lg">
          <Label className="text-sm text-muted-foreground mb-2 block">Session Information</Label>
          <div className="space-y-2 text-xs text-muted-foreground">
            <div className="flex items-center gap-2">
              {loginState.clientInfo.deviceType === 'MOBILE' && <Smartphone className="h-3 w-3" />}
              {loginState.clientInfo.deviceType === 'TABLET' && <Tablet className="h-3 w-3" />}
              {loginState.clientInfo.deviceType === 'DESKTOP' && <Monitor className="h-3 w-3" />}
              <span>Device: {loginState.clientInfo.deviceType}</span>
            </div>
            <div className="flex items-center gap-2">
              <MapPin className="h-3 w-3" />
              <span>Timezone: {loginState.clientInfo.timezone}</span>
            </div>
            <div className="flex items-center gap-2">
              <Clock className="h-3 w-3" />
              <span>Time: {new Date().toLocaleString()}</span>
            </div>
          </div>
        </div>
      </motion.div>
    );
  };

  const renderSecurityFeatures = () => (
    <div className="mt-6 space-y-4">
      {/* Remember Me and 2FA Toggle */}
      <div className="flex items-center justify-between">
        <div className="flex items-center space-x-2">
          <Controller
            name="rememberMe"
            control={control}
            render={({ field }) => (
              <Checkbox
                id="rememberMe"
                checked={field.value}
                onCheckedChange={field.onChange}
              />
            )}
          />
          <Label htmlFor="rememberMe" className="text-sm text-foreground cursor-pointer">
            Remember me
          </Label>
        </div>

        <Button
          type="button"
          variant="ghost"
          size="sm"
          onClick={() => setLoginState(prev => ({ ...prev, show2FA: !prev.show2FA }))}
          className="text-xs text-muted-foreground hover:text-foreground p-0 h-auto"
        >
          {loginState.show2FA ? 'Hide 2FA' : 'Enter 2FA Code'}
        </Button>
      </div>

      {/* Advanced Options Toggle */}
      <div className="text-center">
        <Button
          type="button"
          variant="ghost"
          size="sm"
          onClick={() => setShowAdvancedOptions(!showAdvancedOptions)}
          className="text-xs text-muted-foreground hover:text-foreground"
        >
          {showAdvancedOptions ? 'Hide Advanced Options' : 'Show Advanced Options'}
        </Button>
      </div>
    </div>
  );

  // ================ MAIN RENDER ================

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
              Welcome Back
            </CardTitle>
            <CardDescription className="text-muted-foreground">
              Sign in to access your cosmic dashboard
            </CardDescription>
          </CardHeader>
          
          <CardContent>
            <form onSubmit={handleSubmit(onSubmit)} className="space-y-6">
              {/* Error Alert */}
              {error && (
                <Alert variant={loginState.needsTwoFactor ? "default" : "destructive"}>
                  <AlertCircle className="h-4 w-4" />
                  <AlertDescription>{error}</AlertDescription>
                </Alert>
              )}

              {/* Recent Logins */}
              {renderRecentLogins()}
              
              {/* Username/Email Field */}
              <div className="space-y-2">
                <Label htmlFor="username" className="text-foreground flex items-center gap-2">
                  {loginState.isEmailLogin ? <Mail className="h-4 w-4" /> : <User className="h-4 w-4" />}
                  Username or Email
                </Label>
                <Input
                  id="username"
                  type="text"
                  placeholder="Enter your username or email"
                  className="bg-input border-border text-foreground"
                  {...register('username')}
                />
                {renderLoginTypeIndicator()}
                {errors.username && (
                  <p className="text-sm text-destructive flex items-center gap-1">
                    <AlertCircle className="h-3 w-3" />
                    {errors.username.message}
                  </p>
                )}
              </div>
              
              {/* Password Field */}
              <div className="space-y-2">
                <div className="flex items-center justify-between">
                  <Label htmlFor="password" className="text-foreground flex items-center gap-2">
                    <Lock className="h-4 w-4" />
                    Password
                  </Label>
                  <Button
                    type="button"
                    variant="ghost"
                    size="sm"
                    onClick={handleForgotPassword}
                    className="text-xs text-muted-foreground hover:text-foreground p-0 h-auto"
                  >
                    Forgot password?
                  </Button>
                </div>
                <div className="relative">
                  <Input
                    id="password"
                    type={loginState.showPassword ? 'text' : 'password'}
                    placeholder="Enter your password"
                    className="bg-input border-border text-foreground pr-10"
                    {...register('password')}
                  />
                  <Button
                    type="button"
                    variant="ghost"
                    size="sm"
                    className="absolute right-0 top-0 h-full px-3 py-2 hover:bg-transparent"
                    onClick={() => setLoginState(prev => ({ ...prev, showPassword: !prev.showPassword }))}
                  >
                    {loginState.showPassword ? (
                      <EyeOff className="h-4 w-4 text-muted-foreground" />
                    ) : (
                      <Eye className="h-4 w-4 text-muted-foreground" />
                    )}
                  </Button>
                </div>
                {errors.password && (
                  <p className="text-sm text-destructive flex items-center gap-1">
                    <AlertCircle className="h-3 w-3" />
                    {errors.password.message}
                  </p>
                )}
              </div>

              {/* Two-Factor Authentication */}
              <AnimatePresence>
                {renderTwoFactorField()}
              </AnimatePresence>

              {/* Security Features */}
              {renderSecurityFeatures()}

              {/* Advanced Options */}
              <AnimatePresence>
                {renderAdvancedOptions()}
              </AnimatePresence>
              
              {/* Submit Button */}
              <Button
                type="submit"
                className="w-full bg-mystical-mid hover:bg-mystical-bright text-white"
                disabled={isSubmitting || isLoading || !isValid}
              >
                {isSubmitting || isLoading ? (
                  <>
                    <Loader2 className="mr-2 h-4 w-4 animate-spin" />
                    Signing In...
                  </>
                ) : loginState.needsTwoFactor ? (
                  <>
                    <Shield className="mr-2 h-4 w-4" />
                    Verify & Sign In
                  </>
                ) : (
                  <>
                    <CheckCircle className="mr-2 h-4 w-4" />
                    Sign In
                  </>
                )}
              </Button>
            </form>
            
            {/* Footer Links */}
            <div className="mt-8 space-y-4 text-center">
              <div className="flex items-center justify-center">
                <Separator className="flex-1 bg-mystical-mid/20" />
                <span className="px-4 text-sm text-muted-foreground">New to Cosmic Astrology?</span>
                <Separator className="flex-1 bg-mystical-mid/20" />
              </div>
              
              <Link 
                to="/signup" 
                className="inline-flex items-center gap-2 text-celestial-mid hover:text-celestial-bright font-medium transition-colors"
              >
                Create your account
                <Star className="h-4 w-4" />
              </Link>
              
              <div className="pt-4">
                <Link 
                  to="/" 
                  className="text-muted-foreground hover:text-foreground text-sm transition-colors"
                >
                  ← Back to home
                </Link>
              </div>
            </div>

            {/* Trust Indicators */}
            <div className="mt-6 p-4 bg-muted/30 rounded-lg border border-mystical-mid/10">
              <div className="flex items-center justify-center gap-4 text-xs text-muted-foreground">
                <div className="flex items-center gap-1">
                  <Shield className="h-3 w-3" />
                  <span>Secure Login</span>
                </div>
                <div className="flex items-center gap-1">
                  <Lock className="h-3 w-3" />
                  <span>Encrypted</span>
                </div>
                <div className="flex items-center gap-1">
                  <CheckCircle className="h-3 w-3" />
                  <span>Privacy Protected</span>
                </div>
              </div>
            </div>
          </CardContent>
        </Card>
      </motion.div>
    </div>
  );
}
