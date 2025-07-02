import { User } from './user.model';

export interface Diary {
  id?: number;
  username: User;
  date: Date;
  thought: string;
  mood: string;
  countnegative: number;
}