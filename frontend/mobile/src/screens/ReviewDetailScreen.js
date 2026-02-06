
import React, { useEffect, useState } from 'react';
import { View, Text, StyleSheet, ScrollView, ActivityIndicator } from 'react-native';
import apiService from '../api/apiService';

const ReviewDetailScreen = ({ route }) => {
    const { reviewId } = route.params || {};
    const [review, setReview] = useState(null);
    const [isLoading, setIsLoading] = useState(true);

    useEffect(() => {
        const fetchReview = async () => {
            try {
                const response = await apiService.getReview(reviewId);
                setReview(response.data);
            } catch (error) {
                console.error('Failed to fetch review:', error);
            } finally {
                setIsLoading(false);
            }
        };

        if (reviewId) fetchReview();
    }, [reviewId]);

    if (isLoading) {
        return (
            <View style={styles.centered}>
                <ActivityIndicator size="large" color="#3b82f6" />
            </View>
        );
    }

    if (!review) {
        return (
            <View style={styles.centered}>
                <Text>후기를 찾을 수 없습니다.</Text>
            </View>
        );
    }

    return (
        <ScrollView style={styles.container}>
            <View style={styles.header}>
                <View style={styles.categoryContainer}>
                    <Text style={styles.jobCategory}>{review.jobCategory}</Text>
                    <Text style={styles.rating}>⭐ {review.rating}</Text>
                </View>
                <Text style={styles.title}>{review.title}</Text>
                <View style={styles.meta}>
                    <Text style={styles.nickname}>작성자 ID: {review.memberId}</Text>
                    <Text style={styles.date}>{new Date(review.createdAt).toLocaleDateString()}</Text>
                </View>
            </View>

            <View style={styles.section}>
                <Text style={styles.sectionTitle}>취업 정보</Text>
                <View style={styles.infoRow}>
                    <Text style={styles.label}>준비 기간</Text>
                    <Text style={styles.value}>{review.preparationPeriod}</Text>
                </View>
                <View style={styles.infoRow}>
                    <Text style={styles.label}>기술 스택</Text>
                    <Text style={styles.value}>{review.techStack}</Text>
                </View>
            </View>

            <View style={styles.section}>
                <Text style={styles.sectionTitle}>후기 내용</Text>
                <Text style={styles.content}>{review.content}</Text>
            </View>

            {review.tips ? (
                <View style={styles.section}>
                    <Text style={styles.sectionTitle}>취업 팁</Text>
                    <View style={styles.tipBox}>
                        <Text style={styles.tips}>{review.tips}</Text>
                    </View>
                </View>
            ) : null}
        </ScrollView>
    );
};

const styles = StyleSheet.create({
    container: {
        flex: 1,
        backgroundColor: '#fff',
    },
    centered: {
        flex: 1,
        justifyContent: 'center',
        alignItems: 'center',
    },
    header: {
        padding: 24,
        borderBottomWidth: 1,
        borderBottomColor: '#f1f5f9',
    },
    categoryContainer: {
        flexDirection: 'row',
        justifyContent: 'space-between',
        alignItems: 'center',
        marginBottom: 12,
    },
    jobCategory: {
        fontSize: 14,
        fontWeight: 'bold',
        color: '#3b82f6',
        backgroundColor: '#eff6ff',
        paddingHorizontal: 10,
        paddingVertical: 4,
        borderRadius: 6,
    },
    rating: {
        fontSize: 16,
        fontWeight: 'bold',
        color: '#f59e0b',
    },
    title: {
        fontSize: 24,
        fontWeight: 'bold',
        color: '#1e293b',
        marginBottom: 16,
    },
    meta: {
        flexDirection: 'row',
        justifyContent: 'space-between',
    },
    nickname: {
        fontSize: 14,
        color: '#64748b',
    },
    date: {
        fontSize: 14,
        color: '#94a3b8',
    },
    section: {
        padding: 24,
        borderBottomWidth: 1,
        borderBottomColor: '#f8fafc',
    },
    sectionTitle: {
        fontSize: 18,
        fontWeight: 'bold',
        color: '#1e293b',
        marginBottom: 16,
    },
    infoRow: {
        flexDirection: 'row',
        marginBottom: 12,
    },
    label: {
        fontSize: 15,
        color: '#64748b',
        width: 100,
    },
    value: {
        fontSize: 15,
        fontWeight: '600',
        color: '#334155',
        flex: 1,
    },
    content: {
        fontSize: 16,
        lineHeight: 26,
        color: '#334155',
    },
    tipBox: {
        backgroundColor: '#f8fafc',
        padding: 16,
        borderRadius: 8,
        borderLeftWidth: 4,
        borderLeftColor: '#3b82f6',
    },
    tips: {
        fontSize: 15,
        fontStyle: 'italic',
        color: '#475569',
    },
});

export default ReviewDetailScreen;
