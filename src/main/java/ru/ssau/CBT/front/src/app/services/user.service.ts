import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { User} from '../models/user.model';
import { PagedModel } from '../models/paged-model.model';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private apiUrl = '/rest/admin-ui/users';

  constructor(private http: HttpClient) { }

  getAll(page: number = 0, size: number = 10): Observable<PagedModel<User>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());
    return this.http.get<PagedModel<User>>(this.apiUrl, { params });
  }

  getOne(id: string): Observable<User> {
    return this.http.get<User>(`${this.apiUrl}/${id}`);
  }

  getMany(ids: string[]): Observable<User[]> {
    const params = new HttpParams().set('ids', ids.join(','));
    return this.http.get<User[]>(`${this.apiUrl}/by-ids`, { params });
  }

  create(user: User): Observable<User> {
    return this.http.post<User>(this.apiUrl, user);
  }
}