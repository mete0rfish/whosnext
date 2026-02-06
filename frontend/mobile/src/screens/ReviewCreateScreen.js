
import React, { useState } from 'react';
import { View, Text, StyleSheet, TextInput, TouchableOpacity, ScrollView, Alert } from 'react-native';
import apiService from '../api/apiService';

const ReviewCreateScreen = ({ navigation, route }) => {
    const { companyId, companyName } = route.params || {};

    const [title, setTitle] = useState('');
    const [content, setContent] = useState('');
    const [tips, setTips] = useState('');
    const [rating, setRating] = useState('5');
    const [isLoading, setIsLoading] = useState(false);

    const handleSubmit = async () => {
        if (!title || !content || !rating || !companyId) {
            Alert.alert('오류', '필수 정보를 모두 입력해주세요.');
            return;
        }

        setIsLoading(true);
        try {
            await apiService.createReview({
                companyId,
                title,
                content,
                tips,
                rating: parseInt(rating)
            });
            Alert.alert('성공', '후기가 등록되었습니다!', [
                { text: '확인', onPress: () => navigation.popToTop() }
            ]);
        } catch (error) {
            console.error('Failed to create review:', error);
            Alert.alert('오류', '후기 등록에 실패했습니다.');
        } finally {
            setIsLoading(false);
        }
    };

    return (
        <ScrollView contentContainerStyle={styles.container}>
            <Text style={styles.title}>{companyName ? `${companyName} 후기 작성` : '후기 작성'}</Text>

            <View style={styles.formGroup}>
                <Text style={styles.label}>제목</Text>
                <TextInput
                    style={styles.input}
                    value={title}
                    onChangeText={setTitle}
                    placeholder="예: 면접 분위기가 매우 좋았습니다"
                />
            </View>

            <View style={styles.formGroup}>
                <Text style={styles.label}>내용</Text>
                <TextInput
                    style={[styles.input, styles.textArea]}
                    value={content}
                    onChangeText={setContent}
                    placeholder="상세한 후기를 남겨주세요"
                    multiline
                    numberOfLines={4}
                />
            </View>

            <View style={styles.formGroup}>
                <Text style={styles.label}>팁 (선택)</Text>
                <TextInput
                    style={styles.input}
                    value={tips}
                    onChangeText={setTips}
                    placeholder="다음 지원자를 위한 팁"
                />
            </View>

            <View style={styles.formGroup}>
                <Text style={styles.label}>평점 (1-5)</Text>
                <View style={styles.ratingContainer}>
                    {['1', '2', '3', '4', '5'].map((num) => (
                        <TouchableOpacity
                            key={num}
                            style={[styles.ratingButton, rating === num && styles.ratingButtonActive]}
                            onPress={() => setRating(num)}
                        >
                            <Text style={[styles.ratingText, rating === num && styles.ratingTextActive]}>{num}</Text>
                        </TouchableOpacity>
                    ))}
                </View>
            </View>

            <TouchableOpacity
                style={[styles.button, isLoading && styles.buttonDisabled]}
                onPress={handleSubmit}
                disabled={isLoading}
            >
                <Text style={styles.buttonText}>{isLoading ? '등록 중...' : '후기 등록'}</Text>
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
    textArea: {
        height: 120,
        textAlignVertical: 'top',
    },
    ratingContainer: {
        flexDirection: 'row',
        justifyContent: 'space-between',
    },
    ratingButton: {
        flex: 1,
        backgroundColor: '#fff',
        borderWidth: 1,
        borderColor: '#e2e8f0',
        borderRadius: 8,
        paddingVertical: 10,
        marginHorizontal: 4,
        alignItems: 'center',
    },
    ratingButtonActive: {
        backgroundColor: '#3b82f6',
        borderColor: '#3b82f6',
    },
    ratingText: {
        fontSize: 16,
        color: '#64748b',
        fontWeight: 'bold',
    },
    ratingTextActive: {
        color: '#fff',
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

export default ReviewCreateScreen;
