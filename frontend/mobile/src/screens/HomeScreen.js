
import React from 'react';
import { View, Text, StyleSheet, TouchableOpacity } from 'react-native';
import { useAuth } from '../context/AuthContext';

const HomeScreen = ({ navigation }) => {
    const { user } = useAuth();

    return (
        <View style={styles.container}>
            <View style={styles.hero}>
                <Text style={styles.title}>당신의 다음 커리어는 어디인가요?</Text>
                <Text style={styles.subtitle}>졸업생들의 취업 현황을 공유하고 함께 성장하세요.</Text>
                <TouchableOpacity
                    style={styles.button}
                    onPress={() => navigation.navigate('Companies')}
                >
                    <Text style={styles.buttonText}>후기 보러가기</Text>
                </TouchableOpacity>
            </View>
        </View>
    );
};

const styles = StyleSheet.create({
    container: {
        flex: 1,
        backgroundColor: '#f8fafc',
        padding: 20,
        justifyContent: 'center',
    },
    hero: {
        alignItems: 'center',
        paddingVertical: 60,
    },
    title: {
        fontSize: 28,
        fontWeight: 'bold',
        textAlign: 'center',
        marginBottom: 16,
        color: '#1e293b',
    },
    subtitle: {
        fontSize: 16,
        color: '#64748b',
        textAlign: 'center',
        marginBottom: 32,
    },
    button: {
        backgroundColor: '#3b82f6',
        paddingHorizontal: 24,
        paddingVertical: 12,
        borderRadius: 8,
    },
    buttonText: {
        color: '#fff',
        fontSize: 16,
        fontWeight: '600',
    },
});

export default HomeScreen;
