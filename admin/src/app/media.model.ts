export interface Media {
  id: number;
  name: string;
  categoriesId: number[];
}

export interface MediaCommand {
  id: number;
  categoriesId: number[];
}
