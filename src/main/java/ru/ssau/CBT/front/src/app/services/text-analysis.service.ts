import { Injectable } from '@angular/core';

export interface TextAnalysisResult {
  originalText: string;
  negativeCount: number;
  highlightedText: string;
}

@Injectable({
  providedIn: 'root'
})
export class TextAnalysisService {
  
  private readonly NEGATIVE_WORDS = [
    'плохо', 'ужасно', 'кошмар', 'беда', 'проблема', 'трудность', 'сложно',
    'неудача', 'поражение', 'ошибка', 'вина', 'стыд', 'страх', 'ужас',
    'тревога', 'беспокойство', 'волнение', 'паника', 'депрессия', 'грусть',
    'печаль', 'тоска', 'одиночество', 'отчаяние', 'безнадежность', 'бессилие',
    'усталость', 'изнеможение', 'разочарование', 'обида', 'злость', 'гнев',
    'раздражение', 'ненависть', 'отвращение', 'неуверенность', 'сомнение',
    'не', 'нет', 'никогда', 'ничего', 'никто', 'нигде', 'никак'
  ];

  analyzeText(text: string): TextAnalysisResult {
    if (!text || text.trim() === '') {
      return {
        originalText: text,
        negativeCount: 0,
        highlightedText: text
      };
    }

    console.log('Анализируем текст:', text); // Отладочная информация

    let negativeCount = 0;
    let highlightedText = text;
    const foundWords: string[] = [];

    for (const negativeWord of this.NEGATIVE_WORDS) {
      // Используем более простой подход без границ слов для русского языка
      const regex = new RegExp(this.escapeRegExp(negativeWord), 'gi');
      const matches = text.match(regex);
      
      if (matches) {
        negativeCount += matches.length;
        foundWords.push(...matches);
        
        // Подсветка негативных слов
        highlightedText = highlightedText.replace(
          regex,
          '<span class="negative-word">$&</span>'
        );
      }
    }

    console.log('Найдено негативных слов:', foundWords); // Отладочная информация
    console.log('Общее количество:', negativeCount);

    return {
      originalText: text,
      negativeCount,
      highlightedText
    };
  }

  private escapeRegExp(string: string): string {
    return string.replace(/[.*+?^${}()|[\]\\]/g, '\\$&');
  }
} 