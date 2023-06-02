export class CountDownLatch {
    private count: number;
    private resolve: () => void;
    private readonly promise: Promise<void>;

    constructor() {
        this.count = 0;
        this.promise = new Promise<void>((resolve) => {
            this.resolve = resolve;
        });
    }

    public add(): void {
        this.count++;
    }

    public cutDown(): void {
        this.count--;
        if (this.count === 0) {
            this.resolve();
        }
    }

    public await(): Promise<void> {
        return this.promise;
    }
}