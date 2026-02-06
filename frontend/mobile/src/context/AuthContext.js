
import React, { createContext, useState, useContext, useEffect } from 'react';
import apiService from '../api/apiService';

const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
    const [user, setUser] = useState(null);
    const [isLoading, setIsLoading] = useState(true);

    const checkLoginStatus = async () => {
        try {
            const response = await apiService.getMyProfile();
            setUser(response.data);
        } catch (error) {
            setUser(null);
        } finally {
            setIsLoading(false);
        }
    };

    const login = (userData) => {
        setUser(userData);
    };

    const logout = async () => {
        try {
            await apiService.logout();
            setUser(null);
        } catch (error) {
            console.error('Logout failed:', error);
            setUser(null); // Force logout locally anyway
        }
    };

    useEffect(() => {
        checkLoginStatus();
    }, []);

    return (
        <AuthContext.Provider value={{ user, isLoading, login, logout, checkLoginStatus }}>
            {children}
        </AuthContext.Provider>
    );
};

export const useAuth = () => useContext(AuthContext);
