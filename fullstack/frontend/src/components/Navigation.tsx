import { useState } from 'react';
import { Link, useNavigate, useLocation } from 'react-router-dom';
import { Button } from './ui/button';
import { Menu, X, Star, Moon, Calendar, User, BookOpen, LogOut } from 'lucide-react';
import { motion, AnimatePresence } from 'framer-motion';
import { useAuth } from '../contexts/AuthContext';

export function Navigation() {
  const [isOpen, setIsOpen] = useState(false);
  const { user, logout } = useAuth();
  const navigate = useNavigate();
  const location = useLocation();

  const handleLogout = async () => {
    await logout();
    navigate('/');
  };

  const navItems = user ? [
    { name: 'Dashboard', href: '/dashboard', icon: <Star className="h-4 w-4" /> },
    { name: 'Birth Chart', href: '/birth-chart', icon: <Moon className="h-4 w-4" /> },
    { name: 'Advanced Tools', href: '/advanced-tools', icon: <Calendar className="h-4 w-4" /> },
    { name: 'Transformations', href: '/transformations', icon: <User className="h-4 w-4" /> },
  ] : [];

  return (
    <nav className="fixed top-0 w-full z-50 bg-space-dark/80 backdrop-blur-sm border-b border-mystical-mid/20">
      <div className="max-w-7xl mx-auto px-6">
        <div className="flex items-center justify-between h-16">
          {/* Logo */}
          <Link to={user ? '/dashboard' : '/'} className="flex items-center gap-2">
            <div className="p-2 rounded-full bg-gradient-mystical">
              <Star className="h-6 w-6 text-white" />
            </div>
            <span className="text-xl font-playfair font-bold text-foreground">
              Celestial Sage
            </span>
          </Link>

          {/* Desktop Navigation */}
          <div className="hidden md:flex items-center gap-8">
            {user ? (
              <>
                {navItems.map((item) => (
                  <Link
                    key={item.name}
                    to={item.href}
                    className={`flex items-center gap-2 transition-colors duration-200 ${
                      location.pathname === item.href 
                        ? 'text-celestial-mid' 
                        : 'text-muted-foreground hover:text-celestial-mid'
                    }`}
                  >
                    {item.icon}
                    <span>{item.name}</span>
                  </Link>
                ))}
                <div className="flex items-center gap-3">
                  <span className="text-sm text-muted-foreground">
                    Welcome, {user.name}
                  </span>
                  <Button 
                    variant="ghost" 
                    size="sm"
                    onClick={handleLogout}
                    className="text-muted-foreground hover:text-celestial-mid"
                  >
                    <LogOut className="h-4 w-4" />
                  </Button>
                </div>
              </>
            ) : (
              <>
                <Link 
                  to="/login"
                  className="text-muted-foreground hover:text-celestial-mid transition-colors duration-200"
                >
                  Sign In
                </Link>
                <Link to="/signup">
                  <Button className="bg-celestial-mid hover:bg-celestial-bright text-space-void">
                    Get Started
                  </Button>
                </Link>
              </>
            )}
          </div>

          {/* Mobile Menu Button */}
          <Button
            variant="ghost"
            size="sm"
            onClick={() => setIsOpen(!isOpen)}
            className="md:hidden text-foreground"
          >
            {isOpen ? <X className="h-6 w-6" /> : <Menu className="h-6 w-6" />}
          </Button>
        </div>

        {/* Mobile Navigation */}
        <AnimatePresence>
          {isOpen && (
            <motion.div
              initial={{ opacity: 0, height: 0 }}
              animate={{ opacity: 1, height: "auto" }}
              exit={{ opacity: 0, height: 0 }}
              className="md:hidden border-t border-mystical-mid/20"
            >
              <div className="py-4 space-y-2">
                {user ? (
                  <>
                    {navItems.map((item) => (
                      <Link
                        key={item.name}
                        to={item.href}
                        onClick={() => setIsOpen(false)}
                        className={`flex items-center gap-3 px-4 py-3 rounded-lg transition-colors duration-200 ${
                          location.pathname === item.href 
                            ? 'text-celestial-mid bg-mystical-mid/10' 
                            : 'text-muted-foreground hover:text-celestial-mid hover:bg-mystical-mid/10'
                        }`}
                      >
                        {item.icon}
                        <span>{item.name}</span>
                      </Link>
                    ))}
                    <div className="px-4 pt-2 space-y-2">
                      <div className="text-sm text-muted-foreground">
                        Welcome, {user.name}
                      </div>
                      <Button 
                        variant="outline" 
                        className="w-full border-mystical-mid/30 text-mystical-bright hover:bg-mystical-mid/10"
                        onClick={() => {
                          setIsOpen(false);
                          handleLogout();
                        }}
                      >
                        <LogOut className="mr-2 h-4 w-4" />
                        Sign Out
                      </Button>
                    </div>
                  </>
                ) : (
                  <div className="px-4 space-y-2">
                    <Link to="/login" onClick={() => setIsOpen(false)}>
                      <Button variant="ghost" className="w-full justify-start text-muted-foreground hover:text-celestial-mid">
                        Sign In
                      </Button>
                    </Link>
                    <Link to="/signup" onClick={() => setIsOpen(false)}>
                      <Button className="w-full bg-celestial-mid hover:bg-celestial-bright text-space-void">
                        Get Started
                      </Button>
                    </Link>
                  </div>
                )}
              </div>
            </motion.div>
          )}
        </AnimatePresence>
      </div>
    </nav>
  );
}