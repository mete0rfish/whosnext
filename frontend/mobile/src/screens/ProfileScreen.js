
import React from 'react';
import { View, Text, StyleSheet, TouchableOpacity } from 'react-native';
import { useAuth } from '../context/AuthContext';

const ProfileScreen = () => {
    const { user, logout } = useAuth();

    return (
        <View style={styles.container}>
            <Text style={styles.title}>내 프로필</Text>
            <View style={styles.card}>
                <View style={styles.row}>
                    <Text style={styles.label}>이메일</Text>
                    <Text style={styles.value}>{user?.email || 'N/A'}</Text>
                </View>
                <View style={styles.row}>
                    <Text style={styles.label}>닉네임</Text>
                    <Text style={styles.value}>{user?.nickname || 'N/A'}</Text>
                </View>
            </View>
            <TouchableOpacity style={styles.logoutButton} onPress={logout}>
                <Text style={styles.logoutButtonText}>로그아웃</Text>
            </TouchableOpacity>
        </View>
    );
};

const styles = StyleSheet.create({
    container: {
        flex: 1,
        backgroundColor: '#f8fafc',
        padding: 20,
    },
    title: {
        fontSize: 24,
        fontWeight: 'bold',
        color: '#1e293b',
        marginBottom: 24,
        marginTop: 20,
    },
    card: {
        backgroundColor: '#fff',
        borderRadius: 12,
        padding: 20,
        marginBottom: 24,
        boxShadow: '0 1px 2px rgba(0,0,0,0.1)',
        elevation: 2,
    },
    row: {
        flexDirection: 'row',
        justifyContent: 'space-between',
        paddingVertical: 12,
        borderBottomWidth: 1,
        borderBottomColor: '#f1f5f9',
    },
    label: {
        fontSize: 16,
        color: '#64748b',
    },
    value: {
        fontSize: 16,
        fontWeight: '600',
        color: '#1e293b',
    },
    logoutButton: {
        backgroundColor: '#ef4444',
        paddingVertical: 14,
        borderRadius: 8,
        alignItems: 'center',
    },
    logoutButtonText: {
        color: '#fff',
        fontSize: 16,
        fontWeight: 'bold',
    },
});

export default ProfileScreen;
