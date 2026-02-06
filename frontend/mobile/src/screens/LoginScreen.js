
import React from 'react';
import { View, Text, StyleSheet, TouchableOpacity, Linking } from 'react-native';
import { useAuth } from '../context/AuthContext';

const LoginScreen = () => {
    const { login } = useAuth();

    const handleKakaoLogin = async () => {
        // In a real app, you would use expo-auth-session or a WebView
        // For now, we'll open the browser to the backend OAuth URL
        const authUrl = 'https://concerned-minni-meteorfish-04bbceb2.koyeb.app/oauth2/authorization/kakao';
        Linking.openURL(authUrl);

        // NOTE: To catch the redirect back to the app, you need to configure schemes in app.json
        // and handle Linking.addEventListener('url', ...)
    };

    return (
        <View style={styles.container}>
            <View style={styles.content}>
                <Text style={styles.title}>WhosNext 로그인</Text>
                <Text style={styles.subtitle}>서비스를 이용하려면 로그인이 필요합니다.</Text>

                <TouchableOpacity style={styles.kakaoButton} onPress={handleKakaoLogin}>
                    <Text style={styles.kakaoButtonText}>카카오 로그인</Text>
                </TouchableOpacity>

                {/* Dev Helper */}
                <TouchableOpacity
                    style={{ marginTop: 40 }}
                    onPress={() => login({ nickname: 'Dev User', email: 'dev@example.com' })}
                >
                    <Text style={{ color: '#94a3b8' }}>[개발용] 임시 로그인</Text>
                </TouchableOpacity>
            </View>
        </View>
    );
};

const styles = StyleSheet.create({
    container: {
        flex: 1,
        backgroundColor: '#f8fafc',
        justifyContent: 'center',
        alignItems: 'center',
        padding: 20,
    },
    content: {
        width: '100%',
        maxWidth: 400,
        backgroundColor: '#fff',
        padding: 40,
        borderRadius: 16,
        alignItems: 'center',
        boxShadow: '0 4px 10px rgba(0,0,0,0.1)',
        elevation: 5,
    },
    title: {
        fontSize: 24,
        fontWeight: 'bold',
        color: '#1e293b',
        marginBottom: 12,
    },
    subtitle: {
        fontSize: 16,
        color: '#64748b',
        textAlign: 'center',
        marginBottom: 40,
    },
    kakaoButton: {
        backgroundColor: '#FEE500',
        width: '100%',
        paddingVertical: 14,
        borderRadius: 8,
        alignItems: 'center',
    },
    kakaoButtonText: {
        color: '#000',
        fontSize: 16,
        fontWeight: 'bold',
    },
});

export default LoginScreen;
