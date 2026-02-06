
import React, { useState } from 'react';
import { View, Text, StyleSheet, TextInput, TouchableOpacity, ScrollView, Alert } from 'react-native';
import apiService from '../api/apiService';

const CompanyCreateScreen = ({ navigation }) => {
    const [name, setName] = useState('');
    const [industry, setIndustry] = useState('');
    const [location, setLocation] = useState('');
    const [isLoading, setIsLoading] = useState(false);

    const handleSubmit = async () => {
        if (!name || !industry || !location) {
            Alert.alert('오류', '모든 필드를 입력해주세요.');
            return;
        }

        setIsLoading(true);
        try {
            await apiService.createCompany({ name, industry, location });
            Alert.alert('성공', '기업이 등록되었습니다!', [
                { text: '확인', onPress: () => navigation.goBack() }
            ]);
        } catch (error) {
            console.error('Failed to create company:', error);
            Alert.alert('오류', '기업 등록에 실패했습니다.');
        } finally {
            setIsLoading(false);
        }
    };

    return (
        <ScrollView contentContainerStyle={styles.container}>
            <Text style={styles.title}>새 기업 등록</Text>

            <View style={styles.formGroup}>
                <Text style={styles.label}>기업명</Text>
                <TextInput
                    style={styles.input}
                    value={name}
                    onChangeText={setName}
                    placeholder="예: 구글 코리아"
                />
            </View>

            <View style={styles.formGroup}>
                <Text style={styles.label}>산업군</Text>
                <TextInput
                    style={styles.input}
                    value={industry}
                    onChangeText={setIndustry}
                    placeholder="예: IT / 소프트웨어"
                />
            </View>

            <View style={styles.formGroup}>
                <Text style={styles.label}>위치</Text>
                <TextInput
                    style={styles.input}
                    value={location}
                    onChangeText={setLocation}
                    placeholder="예: 서울 강남구"
                />
            </View>

            <TouchableOpacity
                style={[styles.button, isLoading && styles.buttonDisabled]}
                onPress={handleSubmit}
                disabled={isLoading}
            >
                <Text style={styles.buttonText}>{isLoading ? '등록 중...' : '등록하기'}</Text>
            </TouchableOpacity>
        </ScrollView>
    );
};

const styles = StyleSheet.create({
    container: {
        flexGrow: 1,
        backgroundColor: '#f8fafc',
        padding: 20,
    },
    title: {
        fontSize: 22,
        fontWeight: 'bold',
        color: '#1e293b',
        marginBottom: 24,
    },
    formGroup: {
        marginBottom: 20,
    },
    label: {
        fontSize: 16,
        fontWeight: '600',
        color: '#475569',
        marginBottom: 8,
    },
    input: {
        backgroundColor: '#fff',
        borderWidth: 1,
        borderColor: '#e2e8f0',
        borderRadius: 8,
        padding: 12,
        fontSize: 16,
        color: '#1e293b',
    },
    button: {
        backgroundColor: '#3b82f6',
        paddingVertical: 14,
        borderRadius: 8,
        alignItems: 'center',
        marginTop: 10,
    },
    buttonDisabled: {
        backgroundColor: '#94a3b8',
    },
    buttonText: {
        color: '#fff',
        fontSize: 16,
        fontWeight: 'bold',
    },
});

export default CompanyCreateScreen;
