// src/services/authService.ts
import api from './api'

export interface User {
  id: string
  email: string
  name: string
  birthDate?: string
  birthTime?: string
  birthPlace?: string
  astroProfile?: {
    sunSign: string
    moonSign: string
    risingSign: string
  }
}

export interface BirthProfileData {
  birthDateTime: string
  birthLocation: string
  birthLatitude?: number
  birthLongitude?: number
  timezone?: string
}

export interface LoginCredentials {
  email: string
  password: string
}

export interface RegisterCredentials {
  name: string
  email: string
  password: string
  birthDate?: string
  birthTime?: string
  birthPlace?: string
}

export interface AuthResponse {
  token: string
  refreshToken: string
  user: User
}

class AuthService {
  // LOGIN
  async login(creds: LoginCredentials): Promise<AuthResponse> {
    const res = await api.post<AuthResponse>('/auth/login', creds)
    const { token, refreshToken, user } = res.data

    localStorage.setItem('token', token)
    localStorage.setItem('refreshToken', refreshToken)
    localStorage.setItem('user', JSON.stringify(user))
    api.defaults.headers.common.Authorization = `Bearer ${token}`

    return res.data
  }

  // REGISTER
  async register(creds: RegisterCredentials): Promise<User> {
    const res = await api.post<{ user: User }>('/auth/register', creds)
    return res.data.user
  }

  // PATCH /user/profile
  async updateBirthProfile(data: BirthProfileData): Promise<User> {
    const res = await api.patch<User>('/user/profile', data)
    const updated = res.data
    localStorage.setItem('user', JSON.stringify(updated))
    return updated
  }

  // GET current user
  getCurrentUser(): User | null {
    const str = localStorage.getItem('user')
    return str ? JSON.parse(str) : null
  }

  // LOGOUT
  logout(): void {
    localStorage.removeItem('token')
    localStorage.removeItem('refreshToken')
    localStorage.removeItem('user')
    delete api.defaults.headers.common.Authorization
    window.location.href = '/login'
  }
}

export const authService = new AuthService()
export default authService
