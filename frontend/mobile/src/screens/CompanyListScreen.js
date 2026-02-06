
import React, { useEffect, useState } from 'react';
import { View, Text, StyleSheet, FlatList, ActivityIndicator, TouchableOpacity } from 'react-native';
import apiService from '../api/apiService';

const CompanyListScreen = ({ navigation }) => {
    const [reviews, setReviews] = useState([]);
    const [isLoading, setIsLoading] = useState(true);

    const mockReviews = [
        {
            id: "123e4567-e89b-12d3-a456-426614174000",
            title: "신입 개발자 합격 후기",
            content: "면접이 압박 면접이었지만 잘 대답했습니다.",
            tips: "기본 CS 지식을 탄탄히 하세요.",
            rating: 5,
            preparationPeriod: "3개월",
            techStack: "Java, Spring Boot",
            jobCategory: "DEVELOPMENT",
            createdAt: "2023-10-27T10:00:00"
        },
        {
            id: "223e4567-e89b-12d3-a456-426614174001",
            title: "UI/UX 디자이너 이직 성공",
            content: "포트폴리오 중심의 질문이 많았습니다.",
            tips: "자신의 프로젝트를 수치로 설명하세요.",
            rating: 4,
            preparationPeriod: "6개월",
            techStack: "React, Node.js",
            jobCategory: "DESIGN",
            createdAt: "2023-10-28T14:30:00"
        }
    ];

    const fetchReviews = async () => {
        try {
            const response = await apiService.getReviews();
            if (response.data && response.data.length > 0) {
                setReviews(response.data);
            } else {
                setReviews(mockReviews);
            }
        } catch (error) {
            console.error('Failed to fetch reviews, using dummy data:', error);
            setReviews(mockReviews);
        } finally {
            setIsLoading(false);
        }
    };

    useEffect(() => {
        fetchReviews();
    }, []);

    const renderItem = ({ item }) => (
        <View style={styles.card}>
            <View style={styles.cardHeader}>
                <Text style={styles.jobCategory}>{item.jobCategory}</Text>
                <Text style={styles.rating}>⭐ {item.rating}</Text>
            </View>
            <Text style={styles.title}>{item.title}</Text>
            <View style={styles.infoRow}>
                <Text style={styles.label}>준비 기간:</Text>
                <Text style={styles.value}>{item.preparationPeriod}</Text>
            </View>
            <View style={styles.infoRow}>
                <Text style={styles.label}>기술 스택:</Text>
                <Text style={styles.value} numberOfLines={1}>{item.techStack}</Text>
            </View>

            <View style={styles.cardFooter}>
                <TouchableOpacity
                    style={styles.detailButton}
                    onPress={() => navigation.navigate('ReviewDetail', { reviewId: item.id })}
                >
                    <Text style={styles.detailButtonText}>상세 보기</Text>
                </TouchableOpacity>
                <TouchableOpacity
                    style={styles.reviewButton}
                    onPress={() => navigation.navigate('ReviewCreate', { companyId: item.companyId })}
                >
                    <Text style={styles.reviewButtonText}>나도 작성하기</Text>
                </TouchableOpacity>
            </View>
        </View>
    );

    if (isLoading) {
        return (
            <View style={styles.centered}>
                <ActivityIndicator size="large" color="#3b82f6" />
            </View>
        );
    }

    return (
        <View style={styles.container}>
            <View style={styles.header}>
                <Text style={styles.headerTitle}>취업 후기</Text>
                <TouchableOpacity
                    style={styles.addButton}
                    onPress={() => navigation.navigate('CompanyCreate')}
                >
                    <Text style={styles.addButtonText}>신규 등록</Text>
                </TouchableOpacity>
            </View>
            <FlatList
                data={reviews}
                keyExtractor={(item) => item.id}
                renderItem={renderItem}
                contentContainerStyle={styles.listContent}
                ListEmptyComponent={<Text style={styles.emptyText}>등록된 후기가 없습니다.</Text>}
            />
        </View>
    );
};

const styles = StyleSheet.create({
    container: {
        flex: 1,
        backgroundColor: '#f1f5f9',
    },
    centered: {
        flex: 1,
        justifyContent: 'center',
        alignItems: 'center',
    },
    header: {
        flexDirection: 'row',
        justifyContent: 'space-between',
        alignItems: 'center',
        padding: 20,
        backgroundColor: '#fff',
        borderBottomWidth: 1,
        borderBottomColor: '#e2e8f0',
    },
    headerTitle: {
        fontSize: 20,
        fontWeight: 'bold',
        color: '#1e293b',
    },
    addButton: {
        backgroundColor: '#3b82f6',
        paddingHorizontal: 16,
        paddingVertical: 8,
        borderRadius: 6,
    },
    addButtonText: {
        color: '#fff',
        fontWeight: '600',
    },
    listContent: {
        padding: 16,
    },
    card: {
        backgroundColor: '#fff',
        padding: 16,
        borderRadius: 12,
        marginBottom: 16,
        boxShadow: '0 1px 2px rgba(0,0,0,0.1)',
        elevation: 2,
    },
    cardHeader: {
        flexDirection: 'row',
        justifyContent: 'space-between',
        alignItems: 'center',
        marginBottom: 8,
    },
    jobCategory: {
        fontSize: 12,
        fontWeight: 'bold',
        color: '#3b82f6',
        backgroundColor: '#eff6ff',
        paddingHorizontal: 8,
        paddingVertical: 2,
        borderRadius: 4,
    },
    rating: {
        fontSize: 14,
        fontWeight: 'bold',
        color: '#f59e0b',
    },
    title: {
        fontSize: 18,
        fontWeight: 'bold',
        color: '#1e293b',
        marginBottom: 12,
    },
    infoRow: {
        flexDirection: 'row',
        marginBottom: 4,
    },
    label: {
        fontSize: 14,
        color: '#64748b',
        width: 70,
    },
    value: {
        fontSize: 14,
        color: '#334155',
        flex: 1,
    },
    cardFooter: {
        flexDirection: 'row',
        justifyContent: 'space-between',
        marginTop: 16,
    },
    detailButton: {
        borderWidth: 1,
        borderColor: '#e2e8f0',
        paddingVertical: 8,
        paddingHorizontal: 16,
        borderRadius: 6,
        flex: 1,
        marginRight: 8,
        alignItems: 'center',
    },
    detailButtonText: {
        color: '#64748b',
        fontWeight: '500',
    },
    reviewButton: {
        backgroundColor: '#f1f5f9',
        paddingVertical: 8,
        paddingHorizontal: 16,
        borderRadius: 6,
        flex: 1,
        marginLeft: 8,
        alignItems: 'center',
    },
    reviewButtonText: {
        color: '#3b82f6',
        fontWeight: 'bold',
    },
    emptyText: {
        textAlign: 'center',
        marginTop: 40,
        color: '#94a3b8',
    },
});

export default CompanyListScreen;
