// (window as any).global = window;
// (window as any).process = { env: { DEBUG: undefined } };
// (window as any).Buffer = [];
// (window as any).Buffer = (window as any).Buffer || require('buffer').Buffer;
(window as any).global = window;
import { Buffer } from 'buffer';

(window as any).process = { env: { DEBUG: undefined } };
(window as any).Buffer = (window as any).Buffer || Buffer;