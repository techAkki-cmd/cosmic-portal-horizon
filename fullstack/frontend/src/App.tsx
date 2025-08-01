import { Toaster } from "@/components/ui/toaster";
import { Toaster as Sonner } from "@/components/ui/sonner";
import { TooltipProvider } from "@/components/ui/tooltip";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import { AuthProvider } from "./contexts/AuthContext";
import { ProtectedRoute } from "./components/ProtectedRoute";

// ================ PAGE IMPORTS ================
import Landing from "./pages/Landing";
import Login from "./pages/Login";
import Signup from "./pages/Signup";
import Profile from "./pages/Profile";
import Dashboard from "./pages/Dashboard";
import BirthChart from "./pages/BirthChart";
import AdvancedTools from "./pages/AdvancedTools";
import Transformations from "./pages/Transformations";
import NotFound from "./pages/NotFound";

// ================ QUERY CLIENT CONFIG ================
const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      staleTime: 1000 * 60 * 5, // 5 minutes
      gcTime: 1000 * 60 * 10, // 10 minutes (formerly cacheTime)
      retry: (failureCount, error: any) => {
        // Don't retry on 4xx errors except 408, 429
        if (error?.status >= 400 && error?.status < 500 && ![408, 429].includes(error.status)) {
          return false;
        }
        return failureCount < 3;
      },
    },
    mutations: {
      retry: 1,
    },
  },
});

// ================ MAIN APP COMPONENT ================
const App = () => {
    console.log('🔍 App component rendering, routes should include /profile');

  return (
    <QueryClientProvider client={queryClient}>
      <AuthProvider>
        <TooltipProvider>
          {/* Toast notifications */}
          <Toaster />
          <Sonner />
          
          <BrowserRouter>
            <Routes>
              {/* ================ PUBLIC ROUTES ================ */}
              <Route path="/" element={<Landing />} />
              <Route path="/login" element={<Login />} />
              <Route path="/signup" element={<Signup />} />
              
              {/* ================ PROTECTED ROUTES ================ */}
              <Route path="/dashboard" element={
                <ProtectedRoute>
                  <Dashboard />
                </ProtectedRoute>
              } />
              
              {/* ✅ FIXED: Added the missing Profile route */}
              <Route path="/profile" element={
                <ProtectedRoute>
                  <Profile />
                </ProtectedRoute>
              } />
              
              <Route path="/birth-chart" element={
                <ProtectedRoute>
                  <BirthChart />
                </ProtectedRoute>
              } />
              
              <Route path="/advanced-tools" element={
                <ProtectedRoute>
                  <AdvancedTools />
                </ProtectedRoute>
              } />
              
              <Route path="/transformations" element={
                <ProtectedRoute>
                  <Transformations />
                </ProtectedRoute>
              } />
              
              {/* ================ UTILITY ROUTES ================ */}
              
              {/* Redirect /home to /dashboard for logged-in users */}
              <Route path="/home" element={<Navigate to="/dashboard" replace />} />
              
              {/* Redirect old paths if any */}
              <Route path="/user/profile" element={<Navigate to="/profile" replace />} />
              <Route path="/charts" element={<Navigate to="/birth-chart" replace />} />
              
              {/* ================ CATCH-ALL ROUTE ================ */}
              {/* This should be last to catch all unmatched routes */}
              <Route path="*" element={<NotFound />} />
            </Routes>
          </BrowserRouter>
        </TooltipProvider>
      </AuthProvider>
    </QueryClientProvider>
  );
};

export default App;
