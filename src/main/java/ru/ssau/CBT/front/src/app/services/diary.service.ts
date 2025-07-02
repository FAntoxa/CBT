import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface DiaryEntry {
  username: string;
  date: string;
  thought: string;
  mood: string;
  countnegative: number;
}

export interface CreateDiaryEntry {
  username: string;
  date: string;
  thought: string;
  mood: string;
  countnegative: number;
}

@Injectable({
  providedIn: 'root'
})
export class DiaryService {
  private apiUrl = '/api/diary';

  constructor(private http: HttpClient) { }

  createEntry(entry: CreateDiaryEntry): Observable<DiaryEntry> {
    return this.http.post<DiaryEntry>(this.apiUrl, entry);
  }

  getEntry(username: string, date: string): Observable<DiaryEntry> {
    return this.http.get<DiaryEntry>(`${this.apiUrl}/${username}/${date}`);
  }

  getPreviousEntry(username: string, date: string): Observable<DiaryEntry> {
    return this.http.get<DiaryEntry>(`${this.apiUrl}/${username}/${date}/previous`);
  }

  getNextEntry(username: string, date: string): Observable<DiaryEntry> {
    return this.http.get<DiaryEntry>(`${this.apiUrl}/${username}/${date}/next`);
  }

  getAllEntries(username: string): Observable<DiaryEntry[]> {
    return this.http.get<DiaryEntry[]>(`${this.apiUrl}/${username}`);
  }
}