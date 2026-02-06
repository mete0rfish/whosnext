import React from 'react';
import { NavigationContainer } from '@react-navigation/native';
import { createStackNavigator } from '@react-navigation/stack';
import { createBottomTabNavigator } from '@react-navigation/bottom-tabs';
import { useAuth } from '../context/AuthContext';
import { ActivityIndicator, View } from 'react-native';

// Screens
import LoginScreen from '../screens/LoginScreen';
import HomeScreen from '../screens/HomeScreen';
import CompanyListScreen from '../screens/CompanyListScreen';
import ProfileScreen from '../screens/ProfileScreen';
import CompanyCreateScreen from '../screens/CompanyCreateScreen';
import ReviewCreateScreen from '../screens/ReviewCreateScreen';
import ReviewDetailScreen from '../screens/ReviewDetailScreen';

const Stack = createStackNavigator();
const Tab = createBottomTabNavigator();
const CompanyStack = createStackNavigator();

const CompanyStackNavigator = () => (
    <CompanyStack.Navigator>
        <CompanyStack.Screen name="CompanyList" component={CompanyListScreen} options={{ title: '취업 후기' }} />
        <CompanyStack.Screen name="CompanyCreate" component={CompanyCreateScreen} options={{ title: '기업 등록' }} />
        <CompanyStack.Screen name="ReviewCreate" component={ReviewCreateScreen} options={{ title: '후기 작성' }} />
        <CompanyStack.Screen name="ReviewDetail" component={ReviewDetailScreen} options={{ title: '후기 상세' }} />
    </CompanyStack.Navigator>
);

const MainTabNavigator = () => (
    <Tab.Navigator screenOptions={{ headerShown: false }}>
        <Tab.Screen name="Home" component={HomeScreen} options={{ headerShown: true }} />
        <Tab.Screen name="Companies" component={CompanyStackNavigator} options={{ title: '취업 후기' }} />
        <Tab.Screen name="Profile" component={ProfileScreen} options={{ headerShown: true }} />
    </Tab.Navigator>
);

const AppNavigator = () => {
    const { user, isLoading } = useAuth();

    if (isLoading) {
        return (
            <View style={{ flex: 1, justifyContent: 'center', alignItems: 'center' }}>
                <ActivityIndicator size="large" color="#3b82f6" />
            </View>
        );
    }

    return (
        <NavigationContainer>
            <Stack.Navigator screenOptions={{ headerShown: false }}>
                {user ? (
                    <Stack.Screen name="Main" component={MainTabNavigator} />
                ) : (
                    <Stack.Screen name="Login" component={LoginScreen} />
                )}
            </Stack.Navigator>
        </NavigationContainer>
    );
};

export default AppNavigator;
